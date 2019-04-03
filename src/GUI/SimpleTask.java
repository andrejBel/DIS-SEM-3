package GUI;

import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class SimpleTask {

    protected abstract boolean compute() throws IOException;

    protected abstract void onSuccess();

    protected abstract void onFail();

    public void execute() throws IOException {
        if (compute()) {
            onSuccess();
        } else {
            onFail();
        }
    }

}
