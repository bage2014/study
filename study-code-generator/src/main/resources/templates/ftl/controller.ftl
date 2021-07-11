import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
* ${myClassInfo.classComment}
*
* Created on '${.now?string('yyyy-MM-dd HH:mm:ss')}'.
*/
@Controller
public class ConfController {

    @Resource
    private ${myClassInfo.className}Service ${myClassInfo.className?uncap_first}Service;

    /**
    * 新增
    */
    @RequestMapping("/insert")
    @ResponseBody
    public ReturnT<String> insert(${myClassInfo.className} ${myClassInfo.className?uncap_first}){
        return ${myClassInfo.className?uncap_first}Service.insert(${myClassInfo.className?uncap_first});
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    @ResponseBody
    public ReturnT<String> delete(int id){
        return ${myClassInfo.className?uncap_first}Service.delete(id);
    }

    /**
    * 更新
    */
    @RequestMapping("/update")
    @ResponseBody
    public ReturnT<String> update(${myClassInfo.className} ${myClassInfo.className?uncap_first}){
        return ${myClassInfo.className?uncap_first}Service.update(${myClassInfo.className?uncap_first});
    }

    /**
    * Load查询
    */
    @RequestMapping("/load")
    @ResponseBody
    public ReturnT<String> load(int id){
        return ${myClassInfo.className?uncap_first}Service.load(id);
    }

    /**
    * 分页查询
    */
    @RequestMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int offset,
                                        @RequestParam(required = false, defaultValue = "10") int pagesize) {
        return ${myClassInfo.className?uncap_first}Service.pageList(offset, pagesize);
    }

}
