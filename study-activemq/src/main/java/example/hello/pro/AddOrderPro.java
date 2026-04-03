package example.hello.pro;

public class AddOrderPro {

    public Object addOrder(Object addOrderParam){

        // validate(addOrderParam);

        //
        Object addOrderContext = buildCOntext(addOrderParam);

        // flow
        xxFlow = factory.getMatchFLow(addOrderContext);

        // zhixing
        return xxFlow.exceute();

        // 业务逻辑；
        // flow1
        new Handler()
                .checkState(addOrderContext)
                .checkState(addOrderContext)
                .addProduct1(addOrderContext);
                .addTrainOrder2(addOrderContext);
                .commitDb(); // 事务


        // flow2
        new Handler()
                .addStep(addOrderContext)
                .addStep(addOrderContext)
                .addStep(addOrderContext);
                .addStep(addOrderContext);
                .commitDb(); // 事务


        // flow2
        new Handler()
                .addStep(addOrderContext)
                .addStep(addOrderContext)
                .addStep(addOrderContext);
                .addStep(addOrderContext);
                .addStdddep(addOrderContext);
                .commitDb(); // 事务


    }

}
