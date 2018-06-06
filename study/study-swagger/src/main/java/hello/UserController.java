package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/user")     // 通过这里配置使下面的映射都在/Strings下，可去除
public class UserController {

  
   

    @ApiOperation(value="创建用户", notes="根据String对象创建用户")
    @ApiImplicitParam(name = "String", value = "用户详细实体String", required = false, dataType = "String")
    @RequestMapping(value="/",method= {RequestMethod.GET})
    public String postString() {
    	
    	System.out.println("福建省会计法开始交付时间");
        return "success";
    }

}
