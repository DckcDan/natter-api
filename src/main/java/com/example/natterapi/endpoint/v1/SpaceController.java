package com.example.natterapi.endpoint.v1;

import com.example.natterapi.domain.Space;
import com.example.natterapi.model.ErrorResponse;
import com.example.natterapi.model.SpaceModel;
import com.example.natterapi.service.SpaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/spaces")
@Api(value = "/v1/spaces")
public class SpaceController {

    @Autowired
    private SpaceService spaceService;

    @ApiOperation(value = "/", notes = "POST a new space",httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SpaceModel.class),
            @ApiResponse(code = 400, message = "Validation Error", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorResponse.class)})
    @PostMapping
    public ResponseEntity<SpaceModel> createSpace(@Valid @RequestBody SpaceModel space, Principal principal) throws URISyntaxException {
        //principal check that the owner of this space is actually the logged in user.
        return ResponseEntity.created((new URI("/spaces/" + space.getId())))
                .body(mapSpaceToSpaceModel(spaceService.createNewSpace(mapSpaceModelToSpace(space))));
    }

    private Space mapSpaceModelToSpace(SpaceModel space) {
        return Space.builder()
                .name(space.getName())
                .userName(space.getOwner())
                .build();
    }

    @ApiOperation(value = "/{id}", notes = "GET space ",httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SpaceModel.class),
            @ApiResponse(code = 400, message = "Validation Error", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Resource Not Found Error", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorResponse.class)})
    @GetMapping("{id}")
    public ResponseEntity<SpaceModel> findSpace(@PathVariable Long id) {
        return (spaceService.findSpace(id))
                .map(space -> ResponseEntity.ok().body(mapSpaceToSpaceModel(space)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private SpaceModel mapSpaceToSpaceModel(Space space) {
        return SpaceModel.builder()
                    .createdDate(space.getCreatedDate())
                    .id(space.getId())
                    .name(space.getName())
                    .owner(space.getUserName())
                    .build();
    }

    @ApiOperation(value = "/", notes = "GET all stapces ", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SpaceModel.class),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorResponse.class)})
    @GetMapping
    public ResponseEntity<List<Space>> getAllSpaces() {
        return ResponseEntity.ok().body(spaceService.findAll());
    }

}
