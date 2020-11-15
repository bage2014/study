import com.sun.jdi.Bootstrap;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.StackFrame;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequestManager;

import java.util.Map;

/**
 * Hello world example for Java Debugging API i.e. JDI. Very basic & simple
 * example.
 *
 *
 */
public class AttachingDebugger {
    static final int DEBUGGER_PORT = 55549;

    public static void main(String[] args) throws Exception {
        AttachingConnector ac = Bootstrap.virtualMachineManager().attachingConnectors()
                .stream()
                .filter(c -> c.name().equals("com.sun.jdi.SocketAttach"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to locate ProcessAttachingConnector"));

        Map<String, Connector.Argument> defaultArgs = ac.defaultArguments();
        Connector.IntegerArgument arg = (Connector.IntegerArgument) defaultArgs
                .get("port");

        arg.setValue(DEBUGGER_PORT);
        defaultArgs.put("port", arg);

        LaunchingConnector launchingConnector
                = Bootstrap.virtualMachineManager().defaultConnector();
        // Get arguments of the launching connector
        Map<String, Connector.Argument> defaultArguments
                = launchingConnector.defaultArguments();
        Connector.Argument mainArg = defaultArguments.get("main");
        Connector.Argument suspendArg = defaultArguments.get("suspend");
        // Set class of main method
        mainArg.setValue("Test");
        suspendArg.setValue("true");

        System.out.println("Debugger is attaching to: 55549");
        VirtualMachine vm = ac.attach(defaultArgs);

        EventRequestManager erm = vm.eventRequestManager();
        erm.createClassPrepareRequest().enable();

        EventSet eventSet = null;
        int lineNumberToPutBreakpoint = 2;
        try {
            while ((eventSet = vm.eventQueue().remove()) != null) {

                for (Event event : eventSet) {

                    /*
                     * If this is ClassPrepareEvent, then set breakpoint
                     */
                    if (event instanceof ClassPrepareEvent) {
                        ClassPrepareEvent evt = (ClassPrepareEvent) event;
                        if ("hello_world".equals(evt.referenceType().name())) {
                            Location location = evt.referenceType().locationsOfLine(lineNumberToPutBreakpoint).get(0);
                            BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
                            bpReq.enable();
                        }
                    }

                    /*
                     * If this is BreakpointEvent, then read & print variables.
                     */
                    if (event instanceof BreakpointEvent) {
                        // disable the breakpoint event
//                        event.request().disable();

                        // Get values of all variables that are visible and print
                        StackFrame stackFrame = ((BreakpointEvent) event).thread().frame(0);
                        Map<LocalVariable, Value> visibleVariables = (Map<LocalVariable, Value>) stackFrame
                                .getValues(stackFrame.visibleVariables());
                        System.out.println("Local Variables =");
                        for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
                            System.out.println("	" + entry.getKey().name() + " = " + entry.getValue());
                        }
                    }
                    vm.resume();

                }

            }
        } catch (VMDisconnectedException e) {
            System.out.println("VM is now disconnected.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Debugger done.");
        vm.dispose();
    }

}