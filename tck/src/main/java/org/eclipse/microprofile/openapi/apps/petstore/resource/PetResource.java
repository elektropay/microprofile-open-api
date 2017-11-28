/**
 * Copyright (c) 2017 Contributors to the Eclipse Foundation
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */

package org.eclipse.microprofile.openapi.apps.petstore.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.callbacks.Callback;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import org.eclipse.microprofile.openapi.apps.petstore.data.PetData;
import org.eclipse.microprofile.openapi.apps.petstore.model.Pet;
import org.eclipse.microprofile.openapi.apps.petstore.model.ApiResponse;
import org.eclipse.microprofile.openapi.apps.petstore.exception.NotFoundException;

import java.io.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

@Path("/pet")
@Schema(
        name = "/pet",
        description = "Operations about pets")
@OpenAPIDefinition(
    info = @Info(
        title = "Pets Operations",
        version = "1.0",
        description = "Operations about pets",
        license = @License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
        contact = @Contact(
            name = "",
            url = "",
            email = "")
    )
)
public class PetResource {

    static PetData petData = new PetData();

    @GET
    @Path("/{petId}")
    @Operation(
            method = "GET",
            summary = "Find pet by ID",
            description = "Returns a pet when ID is less than or equal to 10",
            responses = {
                    @APIResponse(
                        responseCode = "400",
                        description = "Invalid ID supplied",
                        content = @Content(
                            mediaType = "none")
                    ),
                    @APIResponse(
                        responseCode = "404",
                        description = "Pet not found",
                        content = @Content(
                            mediaType = "none")
                    ),
                    @APIResponse(
                        responseCode = "200",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "array", implementation = Pet.class))
                    )
            }
    )
    public Response getPetById(
        @Parameter(
            name = "petId",
            description = "ID of pet that needs to be fetched",
            required = true,
            schema = @Schema(
                implementation = Long.class,
                maximum = "10",
                minimum = "1"))
        @PathParam("petId") Long petId)
    throws NotFoundException {
        Pet pet = petData.getPetById(petId);
        if (pet != null) {
            return Response.ok().entity(pet).build();
        }
        else {
            throw new NotFoundException(404, "Pet not found");
        }
    }

    @GET
    @Path("/{petId}/download")
    @Operation(
        method = "GET",
        summary = "Find pet by ID and download it",
        description = "Returns a pet when ID is less than or equal to 10",
        responses = {
            @APIResponse(
                responseCode = "400",
                description = "Invalid ID supplied",
                content = @Content(mediaType = "none")
            ),
            @APIResponse(
                responseCode = "404",
                description = "Pet not found",
                content = @Content(mediaType = "none")
            )
        }
    )
    public Response downloadFile(
        @Parameter(
            name = "petId",
            description = "ID of pet that needs to be fetched",
            required = true,
            schema = @Schema(
                implementation = Long.class,
                maximum = "10",
                minimum = "1")
        )
        @PathParam("petId") Long petId)
    throws NotFoundException {
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException {
                try {
                    // TODO: write file content to output;
                    output.write("hello, world".getBytes());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return Response.ok(stream, "application/force-download")
        .header("Content-Disposition", "attachment; filename = foo.bar")
        .build();
    }

    @DELETE
    @Path("/{petId}")
    @Operation(
        method = "DELETE",
        summary = "Deletes a pet by ID",
        description = "Returns a pet when ID is less than or equal to 10",
        responses = {
            @APIResponse(
                responseCode = "400",
                description = "Invalid ID supplied"
            ),
            @APIResponse(
                responseCode = "404",
                description = "Pet not found"
            )
        }
    )
    public Response deletePet(
        @Parameter(
            name = "apiKey",
            description = "authentication key to access this method",
            schema = @Schema(type = "String", implementation = String.class))
        @HeaderParam("api_key") String apiKey,
        @Parameter(
            name = "petId",
            description = "ID of pet that needs to be fetched",
            required = true,
            schema = @Schema(
                implementation = Long.class,
                maximum = "10",
                minimum = "1"))
        @PathParam("petId") Long petId) {
            if (petData.deletePet(petId)) {
                return Response.ok().build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(
        method = "POST",
        summary = "Add pet to store",
        description = "Add a new pet to the store",
        responses = {
            @APIResponse(
                responseCode = "400",
                description = "Invalid input",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class))
            )
        },
        requestBody = @RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Pet.class)),
            required = true,
            description = "example of a new pet to add"
        )
    )
    public Response addPet(
        @Parameter(
            name ="addPet",
            description = "Pet to add",
            required = true,
            schema = @Schema(implementation = Pet.class)) Pet pet) {
                Pet updatedPet = petData.addPet(pet);
                return Response.ok().entity(updatedPet).build();
            }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(
        method = "PUT",
        summary = "Update an existing pet",
        description = "Update an existing pet with the given new attributes",
        responses = {
            @APIResponse(
                responseCode = "400",
                description = "Invalid ID supplied",
                content = @Content(mediaType = "application/json")
            ),
            @APIResponse(
                responseCode = "404",
                description = "Pet not found",
                content = @Content(mediaType = "application/json")
            ),
            @APIResponse(
                responseCode = "405",
                description = "Validation exception",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    public Response updatePet(
        @Parameter(
            name ="petAttribute",
            description = "Attribute to update existing pet record",
            required = true,
            schema = @Schema(implementation = Pet.class)) Pet pet) {
                Pet updatedPet = petData.addPet(pet);
                return Response.ok().entity(updatedPet).build();
            }

    @GET
    @Path("/findByStatus")
    @Operation(
        method = "GET",
        summary = "Finds Pets by status",
        description = "Find all the Pets with the given status; Multiple status values can be provided with comma seperated strings",
        responses = {
            @APIResponse(
                responseCode = "400",
                description = "Invalid status value",
                content = @Content(mediaType = "none")
            ),
            @APIResponse(
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "list", implementation = Pet.class))
                )
        }
    )
    public Response findPetsByStatus(
        @Parameter(
            name = "status",
            description = "Status values that need to be considered for filter",
            required = true,
            schema = @Schema(implementation = String.class),
            content = {
                @Content(
                    examples = {
                        @ExampleObject(
                            name = "Available",
                            value = "available",
                            summary = "Retrieves all the pets that are available"),
                        @ExampleObject(
                            name = "Pending",
                            value = "pending",
                            summary = "Retrieves all the pets that are pending to be sold"),
                        @ExampleObject(
                            name = "Sold",
                            value = "sold",
                            summary = "Retrieves all the pets that are sold")
                    }
                )
            },
            allowEmptyValue = true) String status) {
                return Response.ok(petData.findPetByStatus(status)).build();
            }

    @GET
    @Path("/findByTags")
    @Callback(
        name = "tagsCallback",
        callbackUrlExpression = "http://petstoreapp.com/pet",
        operation = @Operation(
            method = "GET",
            summary = "Finds Pets by tags",
            deprecated = true,
            description = "Find Pets by tags; Muliple tags can be provided with comma seperated strings. Use tag1, tag2, tag3 for testing.",
            responses = {
                @APIResponse(
                    responseCode = "400",
                    description = "Invalid tag value",
                    content = @Content(mediaType = "none")
                ),
                @APIResponse(
                    responseCode = "200",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(type = "list", implementation = Pet.class))
                    )
            }
        )
    )
    @Deprecated
    public Response findPetsByTags(
        @HeaderParam("apiKey") String apiKey,
        @Parameter(
            name = "tags",
            description = "Tags to filter by",
            required = true,
            deprecated = true)
        @QueryParam("tags") String tags) {
            return Response.ok(petData.findPetByTags(tags)).build();
        }

    @POST
    @Path("/{petId}")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Operation(
        method = "POST",
        summary = "Updates a pet in the store with form data",
        responses = {
            @APIResponse(
                responseCode = "405",
                description = "Validation exception",
                content = @Content(mediaType = "none")
            )
        }
    )
    public Response updatePetWithForm (
        @Parameter(
            name = "petId",
            description = "ID of pet that needs to be updated",
            required = true)
        @PathParam("petId") Long petId,
        @Parameter(
            name = "name",
            description = "Updated name of the pet")
        @FormParam("name") String name,
        @Parameter(
            name = "status",
            description = "Updated status of the pet")
        @FormParam("status") String status) {
            Pet pet = petData.getPetById(petId);
            if(pet != null) {
                if(name != null && !"".equals(name)){
                    pet.setName(name);
                }
                if(status != null && !"".equals(status)){
                    pet.setStatus(status);
                }
                petData.addPet(pet);
                return Response.ok().build();
            }
            else{
                return Response.status(404).build();
            }
        }
}
