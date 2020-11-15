import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HelloMyDebugger {
    static EventRequestManager eventRequestManager;
    static EventQueue eventQueue;
    static EventSet eventSet;
    private static boolean vmExit = false;

    public static void main(String[] args) throws Exception {
        // 获取虚拟机 管理器
        VirtualMachineManager virtualMachineManager = Bootstrap.virtualMachineManager();

        // 链接 到 虚拟机
        VirtualMachine targetVM = attach(virtualMachineManager);


        Process process = targetVM.process();

        // Register ClassPrepareRequest
        eventRequestManager = targetVM.eventRequestManager();
        ClassPrepareRequest classPrepareRequest
                = eventRequestManager.createClassPrepareRequest();
        classPrepareRequest.addClassFilter("HelloJdi");
        classPrepareRequest.addCountFilter(1);
        classPrepareRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        classPrepareRequest.enable();

        // Enter event loop
        eventLoop(targetVM);

        process.destroy();
    }

    private static VirtualMachine attach(VirtualMachineManager virtualMachineManager) throws VMStartException, IllegalConnectorArgumentsException, IOException {
        LaunchingConnector launchingConnector = virtualMachineManager.defaultConnector();
        // Get arguments of the launching connector
        Map<String, Connector.Argument> defaultArguments = launchingConnector.defaultArguments();
        defaultArguments.get("main").setValue("HelloJdi"); // Set class of main method
        defaultArguments.get("suspend").setValue("true");
//        defaultArguments.get("hostname").setValue("localhost");
//        defaultArguments.get("port").setValue("8088");

        VirtualMachine targetVM = launchingConnector.launch(defaultArguments);
        return targetVM;
    }

    private static void eventLoop(VirtualMachine targetVM) throws Exception {
        eventQueue = targetVM.eventQueue();
        while (true) {
            if (vmExit == true) {
                break;
            }
            eventSet = eventQueue.remove();
            EventIterator eventIterator = eventSet.eventIterator();
            while (eventIterator.hasNext()) {
                Event event = (Event) eventIterator.next();
                execute(event);
            }
        }
    }

    private static void execute(Event event) throws Exception {
        if (event instanceof VMStartEvent) {
            System.out.println("VM started");
            eventSet.resume();
        } else if (event instanceof ClassPrepareEvent) {
            ClassPrepareEvent classPrepareEvent = (ClassPrepareEvent) event;
            String mainClassName = classPrepareEvent.referenceType().name();
            if (mainClassName.equals("HelloJdi")) {
                System.out.println("Class " + mainClassName
                        + " is already prepared");
            }
            if (true) {
                // Get location
                ReferenceType referenceType = classPrepareEvent.referenceType();
                List locations = referenceType.locationsOfLine(10);
                Location location = (Location) locations.get(0);

                // Create BreakpointEvent
                BreakpointRequest breakpointRequest = eventRequestManager
                        .createBreakpointRequest(location);
                breakpointRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
                breakpointRequest.enable();
            }
            eventSet.resume();
        } else if (event instanceof BreakpointEvent) {
            System.out.println("Reach line 10 of HelloJdi");
            BreakpointEvent breakpointEvent = (BreakpointEvent) event;
            ThreadReference threadReference = breakpointEvent.thread();
            StackFrame stackFrame = threadReference.frame(0);
            LocalVariable localVariable = stackFrame
                    .visibleVariableByName("str");
            Value value = stackFrame.getValue(localVariable);
            String str = ((StringReference) value).value();
            System.out.println("The local variable str at line 10 is " + str
                    + " of " + value.type().name());
            eventSet.resume();
        } else if (event instanceof VMDisconnectEvent) {
            vmExit = true;
        } else {
            eventSet.resume();
        }
    }

}