package sample.persistence;

import akka.persistence.UntypedPersistentView;

@SuppressWarnings("deprecation")
public class UserView extends UntypedPersistentView {

    private int numReplicated = 0;

    @Override public String persistenceId() {return "user-id-1"; }
    @Override public String viewId() { return "user-view-id-1"; }

    @Override
    public void onReceive(Object message) throws Exception {
        if (isPersistent()) {
            numReplicated += 1;
            System.out.println(String.format("view received %s (num replicated = %d)", message, numReplicated));
        } else {
          unhandled(message);
        }
    }
}