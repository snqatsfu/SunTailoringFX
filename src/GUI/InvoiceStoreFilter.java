package GUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class InvoiceStoreFilter implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String customerText;
    public final boolean showNotDoneOnly;
    public final boolean hideDryCleanOnly;
    public final int dueDateSelectionIndex;
    public final int inDateSelectionIndex;

    public InvoiceStoreFilter(String customerText, boolean showNotDoneOnly, boolean hideDryCleanOnly, int dueDateSelectionIndex, int inDateSelectionIndex) {
        this.customerText = customerText;
        this.showNotDoneOnly = showNotDoneOnly;
        this.hideDryCleanOnly = hideDryCleanOnly;
        this.dueDateSelectionIndex = dueDateSelectionIndex;
        this.inDateSelectionIndex = inDateSelectionIndex;
    }

    public void serialize(ObjectOutputStream os) throws IOException {
        os.writeUTF(customerText);
        os.writeBoolean(showNotDoneOnly);
        os.writeBoolean(hideDryCleanOnly);
        os.writeInt(dueDateSelectionIndex);
        os.writeInt(inDateSelectionIndex);
    }

    public static InvoiceStoreFilter deserialize(ObjectInputStream is) throws IOException {
        String customerText = is.readUTF();
        boolean showNotDoneOnly = is.readBoolean();
        boolean hideDryCleanOnly = is.readBoolean();
        int dueDateSelectionIndex = is.readInt();
        int inDateSelectionIndex = is.readInt();

        return new InvoiceStoreFilter(customerText, showNotDoneOnly, hideDryCleanOnly, dueDateSelectionIndex, inDateSelectionIndex);
    }

}
