import com.sun.jdi.*;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequestManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HelloJdiMyDebugger {

    private static final String TRANSPORT = "dt_socket";
    private static final String HOST = "127.0.0.1";
    private static final int POST = 8088;
    private static final String CLASS_NAME = "HelloJdi";
    private static final int LINE = 13;
    private static final String VARIABLE_NAME = "str";

    public static void main(String[] args) throws Exception {
        // 获取虚拟机 管理器
        VirtualMachineManager virtualMachineManager = Bootstrap.virtualMachineManager();
        // 链接 到 虚拟机
        VirtualMachine targetVM = attach(virtualMachineManager);
        // 设置断点
        Location breakpointLocation = getBreakpointRequestLocation(targetVM);
        // 添加事件
        enableBreakpointRequest(targetVM, breakpointLocation);
        // 处理回调
        handle(targetVM, targetVM.eventQueue());

    }

    private static void handle(VirtualMachine targetVM, EventQueue eventQueue) throws InterruptedException {
        EventQueue evtQueue = targetVM.eventQueue();
        while (true) {
            EventSet evtSet = evtQueue.remove();
            EventIterator eventIterator = evtSet.eventIterator();
            while (eventIterator.hasNext()) {
                try {
                    Event evt = eventIterator.next();
                    doHandle(evt);
                } catch (AbsentInformationException aie) {
                    aie.printStackTrace();
                } catch (Exception exc) {
                    exc.printStackTrace();
                } finally {
                    evtSet.resume();
                }
            }
        }
    }

    private static void enableBreakpointRequest(VirtualMachine targetVM, Location breakpointLocation) {
        EventRequestManager evtReqMgr = targetVM.eventRequestManager();
        BreakpointRequest bReq = evtReqMgr.createBreakpointRequest(breakpointLocation);
        bReq.setSuspendPolicy(BreakpointRequest.SUSPEND_ALL);
        bReq.enable();
    }

    private static void doHandle(Event evt) throws IncompatibleThreadStateException, AbsentInformationException {
        if (!(evt instanceof BreakpointEvent)) {
            return;
        }
        BreakpointEvent brEvt = (BreakpointEvent) evt;
        ThreadReference threadRef = brEvt.thread();
        StackFrame stackFrame = threadRef.frame(0);
        LocalVariable localVariable = stackFrame
                .visibleVariableByName(VARIABLE_NAME);
        Value value = stackFrame.getValue(localVariable);
        System.out.println(VARIABLE_NAME + " = '" + value + "'");

    }

    private static Location getBreakpointRequestLocation(VirtualMachine targetVM) throws AbsentInformationException {
        List<ReferenceType> refTypes = targetVM.allClasses();
        for (ReferenceType referenceType : refTypes) {
            if (referenceType.name().equals(CLASS_NAME)) {
                List<Location> locations = referenceType.allLineLocations();
                for (Location loc : locations) {
                    if (loc.lineNumber() == LINE) {
                        System.out.println("breakpointLocation=" + loc);
                        return loc;
                    }
                }
            }
        }
        return null;
    }

    private static VirtualMachine attach(VirtualMachineManager virtualMachineManager) throws VMStartException, IllegalConnectorArgumentsException, IOException {
        AttachingConnector socketConnector = null;
        List<AttachingConnector> attachingConnectors = virtualMachineManager.attachingConnectors();
        for (AttachingConnector ac : attachingConnectors) {
            if (ac.transport().name().equals(TRANSPORT)) {
                socketConnector = ac;
                break;
            }
        }
        Map paramsMap = socketConnector.defaultArguments();
        Connector.StringArgument hostArg = (Connector.StringArgument) paramsMap.get("hostname");
        hostArg.setValue(HOST);
        Connector.IntegerArgument portArg = (Connector.IntegerArgument) paramsMap.get("port");
        portArg.setValue(String.valueOf(POST));
        VirtualMachine targetVM = socketConnector.attach(paramsMap);
        System.out.print("Attached to process='" + targetVM.name() + "'");
        System.out.print(", description='" + targetVM.description() + "'");
        System.out.println(", JVM version='" + targetVM.version() + "'\n");
        return targetVM;
    }

}