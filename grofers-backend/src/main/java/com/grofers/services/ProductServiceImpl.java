package com.grofers.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.grofers.dtos.ProductDto;
import com.grofers.dtos.ProductResponseDto;
import com.grofers.exceptions.DuplicateEntryException;
import com.grofers.exceptions.NotFoundException;
import com.grofers.exceptions.UserHandlingException;
import com.grofers.pojos.Category;
import com.grofers.pojos.Order;
import com.grofers.pojos.OrderDetail;
import com.grofers.pojos.Product;
import com.grofers.pojos.Supplier;
import com.grofers.pojos.User;
import com.grofers.repos.CategoryRepository;
import com.grofers.repos.ProductRepository;
import com.grofers.repos.SupplierRepository;
import com.grofers.repos.UserRepository;
import com.grofers.securitys.JWTAuthenticationFilter;
import com.grofers.securitys.JWTTokenHelper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {

	@Autowired
	private ProductRepository prodRepo;

	@Autowired
	private SupplierRepository supRepo;

	@Autowired
	private CategoryRepository catRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private JWTTokenHelper jwtTokenHelper;

	@Override
	public ProductResponseDto fetchAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		// Paging and sorting
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> pagedProducts = this.prodRepo.findAll(pageable);

		List<Product> products = pagedProducts.getContent();

		List<ProductDto> productDtos = products.stream().map(p -> this.mapper.map(p, ProductDto.class))
				.collect(Collectors.toList());

		ProductResponseDto resp = new ProductResponseDto();
		resp.setProduct_list(productDtos);
		resp.setPageNumber(pagedProducts.getNumber());
		resp.setPageSize(pagedProducts.getSize());
		resp.setTotalElements(pagedProducts.getTotalElements());
		resp.setTotalPages(pagedProducts.getTotalPages());
		resp.setLastPage(pagedProducts.isLast());

		return resp;
	}

	@Override
	public ProductDto fetchSingleProduct(Integer prodId) {
		Product product = this.prodRepo.findById(prodId)
				.orElseThrow(() -> new NotFoundException("Product with id: " + prodId + " does not exist."));

		return this.mapper.map(product, ProductDto.class);
	}

	@Override
	public ProductDto addNewProduct(ProductDto prodDto, Integer catId, Integer supId) {
		if (prodRepo.existsByProductName(prodDto.getProductName())) {
			throw new DuplicateEntryException("Product name must be unique.");
		} else {

			Category category = this.catRepo.findById(catId)
					.orElseThrow(() -> new NotFoundException("Category with id: " + catId + " does not exist"));

			Supplier supplier = this.supRepo.findById(supId)
					.orElseThrow(() -> new NotFoundException("Supplier with id: " + supId + " does not exist"));

			Product product = this.mapper.map(prodDto, Product.class);
			product.setSupplier(supplier);
			product.setCategory(category);

			Product savedProduct = this.prodRepo.save(product);

			return this.mapper.map(savedProduct, ProductDto.class);
		}
	}

	@Override
	public ProductDto updateProduct(ProductDto prodDto, Integer prodId) {
		Product product = this.prodRepo.findById(prodId)
				.orElseThrow(() -> new NotFoundException("Product with id: " + prodId + " does not exist."));

		// If we want to change the product supplier or category.
		Category category = this.catRepo.findById(prodDto.getCategory().getCategoryId())
				.orElseThrow(() -> new NotFoundException(
						"Category with id: " + prodDto.getCategory().getCategoryId() + " does not exist."));

		Supplier supplier = this.supRepo.findById(prodDto.getSupplier().getSupplierId())
				.orElseThrow(() -> new NotFoundException(
						"Supplier with id: " + prodDto.getSupplier().getSupplierId() + " does not exist."));

		product.setCategory(category);

		product.setSupplier(supplier);

		// Updating the product.
		Product savedProduct = this.prodRepo.save(product);

		return this.mapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public String deleteProduct(Integer prodId) {

		Product product = this.prodRepo.findById(prodId)
				.orElseThrow(() -> new NotFoundException("Product with id: " + prodId + " does not exist."));
		String productName = product.getProductName();
		this.prodRepo.delete(product);

		return "Product with name: " + productName + " was delete successfully !!!";
	}

	@Override
	public List<ProductDto> fetchProductsByCategory(Integer catId) {

		Category category = this.catRepo.findById(catId)
				.orElseThrow(() -> new NotFoundException("Category with id: " + catId + " does not exist"));

		List<Product> products = this.prodRepo.findByCategory(category);

		List<ProductDto> dtos = products.stream().map(p -> this.mapper.map(p, ProductDto.class))
				.collect(Collectors.toList());

		return dtos;
	}

	@Override
	public List<ProductDto> fetchProductsBySupplier(Integer supId) {
		Supplier supplier = this.supRepo.findById(supId)
				.orElseThrow(() -> new NotFoundException("Supplier with id: " + supId + " does not exist"));

		List<Product> products = this.prodRepo.findBySupplier(supplier);

		List<ProductDto> dtos = products.stream().map(p -> this.mapper.map(p, ProductDto.class))
				.collect(Collectors.toList());

		return dtos;
	}

	@Override
	public List<ProductDto> getByNameContaining(String prodName) {

		List<Product> products = this.prodRepo.findByProductNameContaining(prodName);

		List<ProductDto> dtos = products.stream().map(p -> this.mapper.map(p, ProductDto.class))
				.collect(Collectors.toList());

		return dtos;
	}

	@Override
	public List<ProductDto> recommendProducts(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new UserHandlingException("User with given id: " + userId + " does not exist."));

		// Securing against user trying to access orders by some other user.
		String loggedInUserToken = JWTAuthenticationFilter.getTokenChecker();

		String usernameFromToken = this.jwtTokenHelper.getUsernameFromToken(loggedInUserToken);

		if (user.getEmail().equals(usernameFromToken)) {

			Set<Order> orders = user.getOrders();

			Set<Category> catList = new HashSet<>();
			List<Product> result = new ArrayList<>();

			// If there are previous orders by user.
			if (!orders.isEmpty()) {

				orders.forEach(o -> {
					Set<OrderDetail> details = o.getOrderDetails();

					details.forEach((d) -> {
						Category category = d.getProduct().getCategory();
						catList.add(category); // Got all the categories from which the user has previously ordered
												// products
					});

				});

				List<Category> list = catList.stream().collect(Collectors.toList());

				list.forEach((c) -> {
					List<Product> prodList = this.prodRepo.findByCategory(c);
					result.addAll(prodList);
				});

				List<ProductDto> dtos = result.stream().map(p -> this.mapper.map(p, ProductDto.class))
						.collect(Collectors.toList());

				return dtos;
			}
			
			// If there are no orders for the user.
			// Recommend top 2 from each category.

			List<Category> categories = this.catRepo.findAll();
			categories.forEach((c) -> {
				List<Product> prodList = this.prodRepo.findTop2ByCategory(c);
				result.addAll(prodList);
			});

			List<ProductDto> dtos = result.stream().map(p -> this.mapper.map(p, ProductDto.class))
					.collect(Collectors.toList());

			return dtos;
		} else
			throw new UserHandlingException("Invalid userId. Please provide your own userId to get recommendations.");

	}

}
