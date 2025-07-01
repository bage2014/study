import java.util.Map;

/**
* ${myClassInfo.classComment}
*
* Created on '${.now?string('yyyy-MM-dd HH:mm:ss')}'.
*/
public interface ${myClassInfo.className}Service {

    /**
    * 新增
    */
    public ReturnT<String> insert(${myClassInfo.className} ${myClassInfo.className?uncap_first});

    /**
    * 删除
    */
    public ReturnT<String> delete(int id);

    /**
    * 更新
    */
    public ReturnT<String> update(${myClassInfo.className} ${myClassInfo.className?uncap_first});

    /**
    * Load查询
    */
    public ${myClassInfo.className} load(int id);

    /**
    * 分页查询
    */
    public Map<String,Object> pageList(int offset, int pagesize);

}
