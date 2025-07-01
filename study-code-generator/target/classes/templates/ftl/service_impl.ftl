import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* ${myClassInfo.classComment}
*
* Created on '${.now?string('yyyy-MM-dd HH:mm:ss')}'.
*/
@Service
public class ${myClassInfo.className}ServiceImpl implements ${myClassInfo.className}Service {

	@Resource
	private ${myClassInfo.className}Dao ${myClassInfo.className?uncap_first}Dao;

	/**
    * 新增
    */
	@Override
	public ReturnT<String> insert(${myClassInfo.className} ${myClassInfo.className?uncap_first}) {

		// valid
		if (${myClassInfo.className?uncap_first} == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "必要参数缺失");
        }

		${myClassInfo.className?uncap_first}Dao.insert(${myClassInfo.className?uncap_first});
        return ReturnT.SUCCESS;
	}

	/**
	* 删除
	*/
	@Override
	public ReturnT<String> delete(int id) {
		int ret = ${myClassInfo.className?uncap_first}Dao.delete(id);
		return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	* 更新
	*/
	@Override
	public ReturnT<String> update(${myClassInfo.className} ${myClassInfo.className?uncap_first}) {
		int ret = ${myClassInfo.className?uncap_first}Dao.update(${myClassInfo.className?uncap_first});
		return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	* Load查询
	*/
	@Override
	public ${myClassInfo.className} load(int id) {
		return ${myClassInfo.className?uncap_first}Dao.load(id);
	}

	/**
	* 分页查询
	*/
	@Override
	public Map<String,Object> pageList(int offset, int pagesize) {

		List<${myClassInfo.className}> pageList = ${myClassInfo.className?uncap_first}Dao.pageList(offset, pagesize);
		int totalCount = ${myClassInfo.className?uncap_first}Dao.pageListCount(offset, pagesize);

		// result
		Map<String, Object> result = new HashMap<String, Object>();
		maps.put("pageList", pageList);
		maps.put("totalCount", totalCount);

		return result;
	}

}
