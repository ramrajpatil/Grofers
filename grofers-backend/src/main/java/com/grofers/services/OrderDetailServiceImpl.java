package com.grofers.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grofers.dtos.OrderDetailDto;
import com.grofers.exceptions.NotFoundException;
import com.grofers.exceptions.UserHandlingException;
import com.grofers.pojos.Order;
import com.grofers.pojos.OrderDetail;
import com.grofers.pojos.Product;
import com.grofers.repos.OrderDetailRepository;
import com.grofers.repos.OrderRepository;
import com.grofers.repos.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderDetailServiceImpl implements IOrderDetailService {

	@Autowired
	private OrderDetailRepository oDRepo;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private ProductRepository prodRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<OrderDetailDto> fetchOrderDetailsByOrder(Integer orderId) {
		Order order = this.orderRepo.findById(orderId)
				.orElseThrow(() -> new UserHandlingException("Order with given id: " + orderId + " does not exist."));

		List<OrderDetail> details = this.oDRepo.findByOrder(order);

		List<OrderDetailDto> detailDtos = details.stream().map(d -> this.mapper.map(d, OrderDetailDto.class))
				.collect(Collectors.toList());

		return detailDtos;
	}

	@Override
	public List<OrderDetailDto> fetchAllOrderDetails() {

		List<OrderDetail> details = this.oDRepo.findAll();

		List<OrderDetailDto> detailDtos = details.stream().map(d -> this.mapper.map(d, OrderDetailDto.class))
				.collect(Collectors.toList());

		return detailDtos;
	}

	@Override
	public OrderDetailDto fetchSingleOrderDetail(Integer id) {
		OrderDetail detail = this.oDRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Order detail with given id: " + id + " does not exist."));

		return this.mapper.map(detail, OrderDetailDto.class);
	}

	@Override
	public OrderDetailDto addProductToOrder(OrderDetailDto orderDetailDto, Integer orderId, Integer productId) {
		Order order = this.orderRepo.findById(orderId)
				.orElseThrow(() -> new NotFoundException("Order with given id: " + orderId + " does not exist."));
		
		Product product = this.prodRepo.findById(productId)
				.orElseThrow(() -> 
				new NotFoundException("Product with id: "+productId+" does not exist."));
				
		OrderDetail detail = this.mapper.map(orderDetailDto, OrderDetail.class);
		detail.setOrder(order);
		detail.setProduct(product);
		
		OrderDetail savedDetail = this.oDRepo.save(detail);

		return this.mapper.map(savedDetail, OrderDetailDto.class);
	}

	@Override
	public OrderDetailDto updateProductFromOrderDetail(OrderDetailDto orderDetailDto, Integer id) {

		OrderDetail detail = this.oDRepo.findById(id)
		.orElseThrow(() -> 
		new NotFoundException("Order detail with id: "+id+" does not exists."));
		
		Order order = this.orderRepo.findById(orderDetailDto.getOrder().getOrderId())
				.orElseThrow(() -> 
				new NotFoundException("Order with given id: " + orderDetailDto.getOrder().getOrderId() + " does not exist."));
		
		
		Product product = this.prodRepo.findById(orderDetailDto.getProduct().getProductId())
				.orElseThrow(() -> 
				new NotFoundException("Product with id: "+orderDetailDto.getProduct().getProductId()+" does not exist."));
		
		
		detail.setQuantity(orderDetailDto.getQuantity());
		detail.setOrder(order);
		detail.setProduct(product);
		
		OrderDetail updatedDetail = this.oDRepo.save(detail);
		
		return this.mapper.map(updatedDetail, OrderDetailDto.class);
	}

	@Override
	public String removeProductFromOrder(Integer id) {

		OrderDetail detail = this.oDRepo.findById(id)
				.orElseThrow(() -> 
				new NotFoundException("Order detail with id: "+id+" does not exists."));
				
		this.oDRepo.delete(detail);
		
		return "Order detail with id: "+id+" was deleted successfully !!!";
	}

}
