package library.data.model;

public class Member {

	Integer id;
	String name;
	String city;
	String address;
	Long mobile;
	String email;
	
	public Member (Integer id, String name, String city, 
			String address, Long mobile, String email) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.address = address;
		this.mobile = mobile;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
