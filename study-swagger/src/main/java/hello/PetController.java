package hello;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
public class PetController {

//	@ApiOperation(value = "Find pet by Status",
//		      notes = "${SomeController.findPetsByStatus.notes}") 
//		  @RequestMapping(value = "/findByStatus", method = RequestMethod.GET, params = {"status"})
//		  public String findPetsByStatus(
//		      @ApiParam(value = "${SomeController.findPetsByStatus.status}", 
//		           required = true)
//		      @RequestParam(value="status", defaultValue="${SomeController.findPetsByStatus.status.default}") String status) { 
//		      //...
//		return "";
//		  }

		  @ApiOperation(notes = "Operation 2", value = "hhh") 
		  @ApiImplicitParams(
		      @ApiImplicitParam(name="header1", value="fjsjfsj") 
		  )
		  @RequestMapping(value = "operation2", method = RequestMethod.GET)
		  public ResponseEntity<String> operation2(
				  @ApiParam(name = "title", value = "公告标题", required = true) @RequestParam("title") String title
				  
				  ) {
		    return ResponseEntity.ok("heh");
		  }
		  
//	@ApiOperation(value = "Find pet by Status", notes = "notes")
//	@RequestMapping(value = "/findByStatus", method = RequestMethod.GET)
//	@ResponseBody
//	public String findPetsByStatus(
////			@ApiParam(value = "status", required = true)
//			@RequestParam(value = "status", defaultValue = "bage") String status) {
//		System.out.println("status::" + status);
//		return "" + status;
//	}
	
}
