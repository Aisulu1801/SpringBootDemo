package kz.springdemo.CosmeticShop.service;

import kz.springdemo.CosmeticShop.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
Users getUserByEmail(String email);
Users createUser(Users user);
}
