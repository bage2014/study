import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* ${myClassInfo.classComment}
*
* Created on '${.now?string('yyyy-MM-dd HH:mm:ss')}'.
*/
@Component
public interface ${myClassInfo.className}Dao {

    /**
    * 新增
    */
    public int insert(@Param("${myClassInfo.className?uncap_first}") ${myClassInfo.className} ${myClassInfo.className?uncap_first});

    /**
    * 删除
    */
    public int delete(@Param("id") int id);

    /**
    * 更新
    */
    public int update(@Param("${myClassInfo.className?uncap_first}") ${myClassInfo.className} ${myClassInfo.className?uncap_first});

    /**
    * Load查询
    */
    public ${myClassInfo.className} load(@Param("id") int id);

    /**
    * 分页查询Data
    */
	public List<${myClassInfo.className}> pageList(@Param("offset") int offset,
                                                 @Param("pagesize") int pagesize);

    /**
    * 分页查询Count
    */
    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize);

}
