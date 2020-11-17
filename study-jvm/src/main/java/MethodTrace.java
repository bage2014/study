import java.util.Map;
import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

public class MethodTrace {
    private VirtualMachine vm;
    private Process process;
    private EventRequestManager eventRequestManager;
    private EventQueue eventQueue;
    private EventSet eventSet;
    private boolean vmExit = false;
    //write your own testclass
    private String className = "HelloJdi";

    public static void main(String[] args) throws Exception {

        MethodTrace trace = new MethodTrace();
        trace.launchDebugee();
        trace.registerEvent();

        trace.processDebuggeeVM();

        // Enter event loop
        trace.eventLoop();

        trace.destroyDebuggeeVM();

    }

    public void launchDebugee() {
        LaunchingConnector launchingConnector = Bootstrap
                .virtualMachineManager().defaultConnector();

        // Get arguments of the launching connector
        Map<String, Connector.Argument> defaultArguments = launchingConnector
                .defaultArguments();
        Connector.Argument mainArg = defaultArguments.get("main");
        Connector.Argument suspendArg = defaultArguments.get("suspend");

        // Set class of main method
        mainArg.setValue(className);
        suspendArg.setValue("true");
        try {
            vm = launchingConnector.launch(defaultArguments);
        } catch (Exception e) {
            // ignore
        }
    }

    public void processDebuggeeVM() {
        process = vm.process();
    }

    public void destroyDebuggeeVM() {
        process.destroy();
    }

    public void registerEvent() {
        // Register ClassPrepareRequest
        eventRequestManager = vm.eventRequestManager();
        MethodEntryRequest entryReq = eventRequestManager.createMethodEntryRequest();

        entryReq.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
        entryReq.addClassFilter(className);
        entryReq.enable();

        MethodExitRequest exitReq = eventRequestManager.createMethodExitRequest();
        exitReq.addClassFilter(className);
        exitReq.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
        exitReq.enable();
    }

    private void eventLoop() throws Exception {
        eventQueue = vm.eventQueue();
        while (true) {
            if (vmExit == true) {
                break;
            }
            eventSet = eventQueue.remove();
            EventIterator eventIterator = eventSet.eventIterator();
            while (eventIterator.hasNext()) {
                Event event = (Event) eventIterator.next();
                execute(event);
                if (!vmExit) {
                    eventSet.resume();
                }
            }
        }
    }

    private void execute(Event event) throws Exception {
        if (event instanceof VMStartEvent) {
            System.out.println("VM started");
        } else if (event instanceof MethodEntryEvent) {
            Method method = ((MethodEntryEvent) event).method();
            System.out.printf("Enter -> Method: %s, Signature:%s\n",method.name(),method.signature());
            System.out.printf("\t ReturnType:%s\n", method.returnTypeName());
        } else if (event instanceof MethodExitEvent) {
            Method method = ((MethodExitEvent) event).method();
            System.out.printf("Exit -> method: %s\n",method.name());
        } else if (event instanceof VMDisconnectEvent) {
            vmExit = true;
        }
    }
}