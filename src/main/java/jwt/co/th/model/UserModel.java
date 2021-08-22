package jwt.co.th.model;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_table")
public class UserModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter@Setter
	@JsonIgnore
	private int id;

	@Getter@Setter
	@NotBlank(message = "Name is mandatory")
	private String name;

	@Getter@Setter
	@NotBlank(message = "Email is mandatory")
	private String email;

	@Getter@Setter
	@NotBlank(message = "Password is mandatory")
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@Getter@Setter
	@JsonIgnore
	private Set<Role> roles;
	
	@Getter@Setter
	private Boolean active;
	
	@Getter@Setter
	private String token;
	
	@Getter@Setter
	private LocalDateTime  tokenCreationDate;

	@Override
	public String toString() {
		return "UserModel [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", active="
				+ active + ", token=" + token + ", tokenCreationDate=" + tokenCreationDate + "]";
	}


}
