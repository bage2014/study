package com.bage;

// 测试类
public class ObjectMergerTest {
    public static void main(String[] args) {
        // 创建测试对象
        User user1 = new User("张三", 25, "zhangsan@email.com", null);
        User user2 = new User(null, 30, "newemail@email.com", "13800138000");
        User user3 = new User("李四", null, null, "13900139000");
        
        System.out.println("原始对象:");
        System.out.println("User1: " + user1);
        System.out.println("User2: " + user2);
        System.out.println("User3: " + user3);
        
        // 基础合并
        User merged1 = ObjectMerger.mergeObjects(user1, user2);
        System.out.println("\n基础合并结果:");
        System.out.println("Merged: " + merged1);
        
        // 增强合并（忽略null值）
        User user4 = new User("张三", 25, "zhangsan@email.com", null);
        User merged2 = EnhancedObjectMerger.mergeObjects(user4, user2, true);
        System.out.println("\n增强合并（忽略null）:");
        System.out.println("Merged: " + merged2);
        
        // 排除特定字段
        User user5 = new User("张三", 25, "zhangsan@email.com", null);
        User merged3 = EnhancedObjectMerger.mergeObjects(user5, user2, true, "age");
        System.out.println("\n排除age字段合并:");
        System.out.println("Merged: " + merged3);
        
        // 多对象合并
        User merged4 = EnhancedObjectMerger.mergeMultipleObjects(
            new User("初始", 0, "initial@email.com", null),
            new User(null, 30, null, "13800138000"),
            new User("最终", null, "final@email.com", null)
        );
        System.out.println("\n多对象合并:");
        System.out.println("Merged: " + merged4);
    }
}