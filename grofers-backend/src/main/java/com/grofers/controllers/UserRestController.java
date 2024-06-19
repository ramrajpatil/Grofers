package com.grofers.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grofers.config.AppConstants;
import com.grofers.dtos.OrderDto;
import com.grofers.dtos.OrderResponseDto;
import com.grofers.dtos.ProductDto;
import com.grofers.dtos.ResponseDTO;
import com.grofers.dtos.UserDto;
import com.grofers.dtos.UserResponseDto;
import com.grofers.services.IOrderService;
import com.grofers.services.IProductService;
import com.grofers.services.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserRestController {

    private final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private IUserService uService;
    
    @Autowired
    private IProductService prodService;
    
    @Autowired
    private IOrderService orderService;
    
	// Register new user API
	@PostMapping
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {

		UserDto registeredUserDto = this.uService.registerUser(userDto);

		return new ResponseEntity<>(registeredUserDto, HttpStatus.CREATED);
	}

    // GET - getting all users: Only admin can fetch all users.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<UserResponseDto> fetchAllUsers( // default pageNumber:0, pageSize:5, sortBy:userId, sortDir:asc.
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber, 
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.USER_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {

        this.logger.info("In get all Users request of: {}", getClass().getName());
        UserResponseDto userResponseDto = this.uService.fetchAllUsers(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(userResponseDto);
    }

    // GET - get single user
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable Integer userId) {
        UserDto userDto = this.uService.fetchSingleUser(userId);
        return ResponseEntity.ok(userDto);
    }

    // POST - create user: Only admin can create a user. Admin has option to create a customer or admin by specifying so.
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto,
    		@RequestParam(value = "choice",defaultValue = AppConstants.CHOICE ,required = false) String choice) {
    	
        UserDto createdUserDto = this.uService.addNewUser(userDto, choice);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    // PUT - update user: Only admin can update user details.
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUserDetails(@PathVariable Integer userId, @Valid @RequestBody UserDto userDto) {
        logger.info("In updateUserDetails() for userId: {}", userId);
        // Omitting logging password for security reasons
        UserDto updatedUserDto = this.uService.updateUser(userDto, userId);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    // DELETE - delete user: Only admin can delete a user.
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDTO> deleteUserDetails(@PathVariable Integer userId) {
        this.uService.deleteUser(userId);
        return new ResponseEntity<>(new ResponseDTO("User with id: " + userId + " deleted successfully", true), HttpStatus.OK);
    }
    
    // Only customer can access this api.
    @PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/recommend/{userId}")
	public ResponseEntity<List<ProductDto>> recommendProducts(@PathVariable Integer userId){
		
		List<ProductDto> dtos = this.prodService.recommendProducts(userId);
		
		return ResponseEntity.ok(dtos);
	}
    
    @PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/{userId}/orders")
	public ResponseEntity<OrderResponseDto> getAllOrdersByUser(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.ORDER_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
			@PathVariable Integer userId) {
		OrderResponseDto dto = this.orderService.fetchAllOrdersByUser(userId, pageNumber, pageSize, sortBy, sortDir);

		return ResponseEntity.ok(dto);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/order/{orderId}")
	public ResponseEntity<OrderDto> getSingleOrder(@PathVariable Integer orderId) {
		System.out.println("In get single order of: "+getClass().getName());
		OrderDto orderDto = this.orderService.fetchSingleOrder(orderId);

		return ResponseEntity.ok(orderDto);
	}
}
