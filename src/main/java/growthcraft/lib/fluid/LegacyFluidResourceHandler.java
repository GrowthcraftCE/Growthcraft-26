package growthcraft.lib.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LegacyFluidResourceHandler implements ResourceHandler<FluidResource> {
    private final IFluidHandler handler;
    private final boolean canInsert;
    private final boolean canExtract;
    private final SnapshotJournal<List<FluidStack>> journal = new SnapshotJournal<>() {
        @Override
        protected List<FluidStack> createSnapshot() {
            List<FluidStack> snapshot = new ArrayList<>(handler.getTanks());
            for (int i = 0; i < handler.getTanks(); i++) {
                snapshot.add(handler.getFluidInTank(i).copy());
            }
            return snapshot;
        }

        @Override
        protected void revertToSnapshot(List<FluidStack> snapshot) {
            for (int i = 0; i < snapshot.size(); i++) {
                FluidStack current = handler.getFluidInTank(i);
                if (!current.isEmpty()) {
                    handler.drain(current.copyWithAmount(current.getAmount()), IFluidHandler.FluidAction.EXECUTE);
                }
            }

            for (FluidStack stack : snapshot) {
                if (!stack.isEmpty()) {
                    handler.fill(stack.copy(), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    };

    public static ResourceHandler<FluidResource> of(IFluidHandler handler) {
        return of(handler, true, true);
    }

    public static ResourceHandler<FluidResource> of(IFluidHandler handler, boolean canInsert, boolean canExtract) {
        return new LegacyFluidResourceHandler(handler, canInsert, canExtract);
    }

    private LegacyFluidResourceHandler(IFluidHandler handler, boolean canInsert, boolean canExtract) {
        this.handler = handler;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    @Override
    public int size() {
        return handler.getTanks();
    }

    @Override
    public FluidResource getResource(int index) {
        return FluidResource.of(fluidInTank(index));
    }

    @Override
    public long getAmountAsLong(int index) {
        return fluidInTank(index).getAmount();
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        Objects.checkIndex(index, size());
        if (!resource.isEmpty() && !isValid(index, resource)) {
            return 0;
        }
        return handler.getTankCapacity(index);
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        Objects.checkIndex(index, size());
        return canInsert && !resource.isEmpty() && handler.isFluidValid(index, resource.toStack(1));
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, size());
        checkNonEmptyNonNegative(resource, amount);
        if (!canInsert || amount == 0 || !isValid(index, resource)) {
            return 0;
        }

        FluidStack current = handler.getFluidInTank(index);
        if (!current.isEmpty() && !resource.matches(current)) {
            return 0;
        }

        if (transaction == null) {
            try (Transaction tx = Transaction.openRoot()) {
                int inserted = insert(index, resource, amount, tx);
                tx.commit();
                return inserted;
            }
        }

        journal.updateSnapshots(transaction);
        return handler.fill(resource.toStack(amount), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        Objects.checkIndex(index, size());
        checkNonEmptyNonNegative(resource, amount);
        if (!canExtract || amount == 0 || !resource.matches(handler.getFluidInTank(index))) {
            return 0;
        }

        if (transaction == null) {
            try (Transaction tx = Transaction.openRoot()) {
                int extracted = extract(index, resource, amount, tx);
                tx.commit();
                return extracted;
            }
        }

        journal.updateSnapshots(transaction);
        return handler.drain(resource.toStack(amount), IFluidHandler.FluidAction.EXECUTE).getAmount();
    }

    private FluidStack fluidInTank(int index) {
        Objects.checkIndex(index, size());
        return handler.getFluidInTank(index);
    }

    private static void checkNonEmptyNonNegative(FluidResource resource, int amount) {
        if (resource.isEmpty()) {
            throw new IllegalArgumentException("Resource may not be empty.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Amount may not be negative: " + amount);
        }
    }
}
