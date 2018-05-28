package io.swagger.sample.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.sample.data.PetData;
import io.swagger.sample.exception.NotFoundException;
import io.swagger.sample.models.Pet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PetResource extends AbstractResource {
    private static PetData data = new PetData();
    @ApiOperation(notes = "Returns a pet when 0 < ID <= 10.  ID > 10 or non-integers will simulate API error conditions", value = "Find pet by ID", nickname = "getPetById",
        tags = {"Pets"} )
    @ApiResponses({
        @ApiResponse(code = 200, message = "Nice!", response = Pet.class),
        @ApiResponse(code = 400, message = "Invalid ID supplied", response = io.swagger.sample.models.ApiResponse.class),
        @ApiResponse(code = 404, message = "Pet not found", response = io.swagger.sample.models.ApiResponse.class)
    })
    @RequestMapping(value = "/pets/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Pet> getPetById(@ApiParam(value = "ID of pet that needs to be fetched", allowableValues = "range[1,10]", required = true) @PathVariable("id") Integer petId) throws Exception {
        Pet pet = data.getPetById(petId);
        if(pet != null) {
            return ResponseEntity.ok().body(pet);
        }
        else {
            throw new NotFoundException(io.swagger.sample.models.ApiResponse.ERROR, "Pet " + petId + " not found");
        }
    }
}
