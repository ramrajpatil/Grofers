package com.app.pojos;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@Embeddable
public class UserDetails {

	@NotNull(message = "Organization Name cannot be blank")
	@Length(max = 100)
	@Column(name = "org_name", length = 100, nullable = false)
	private String orgName;

	@NotNull(message = "GST number cannot be blank")
	@Length(min = 15, max = 15)
	@Column(name = "gst_number", length = 15, nullable = false, unique = true)
	private String gstNumber;

	@NotNull(message = "Lane Name cannot be blank")
	@Length(max = 30)
	@Column(length = 30, nullable = false)
	private String lane;

	@NotNull(message = "City Name cannot be blank")
	@Length(max = 30)
	@Column(length = 30, nullable = false)
	private String city;

	@NotNull(message = "Taluka Name cannot be blank")
	@Length(max = 30)
	@Column(length = 30, nullable = false)
	private String taluka;

	@NotNull(message = "District Name cannot be blank")
	@Length(max = 30)
	@Column(length = 30, nullable = false)
	private String district;

	@NotNull(message = "State Name cannot be blank")
	@Length(max = 30)
	@Column(length = 30, nullable = false)
	private String state;

	@NotNull(message = "Pincode cannot be blank")
	@Digits(integer = 6, fraction = 0, message = "Pincode must have exactly 6 digits")
	@Column(nullable = false)
	private int pincode;
}