package kz.springdemo.CosmeticShop.controller;

import kz.springdemo.CosmeticShop.entities.*;
import kz.springdemo.CosmeticShop.service.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CosmeticsService cosmeticsService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @Value("${file.photo.upload}")
    private String uploadPath;

    @Value("${file.photo.view}")
    private String viewPath;

    @Value("${file.photo.default}")
    private String defaultPath;

    @Value("${file.photo.upload.target}")
    private String uploadPathTarget;

    @GetMapping(value = "/")
    public String index (Model model,@RequestParam(name = "keyword", required = false)String keyword){
        model.addAttribute("tovary",cosmeticsService.getAllCosmetics(keyword));
        model.addAttribute("currentUser",getUserData());
        model.addAttribute("keyword",keyword);
        model.addAttribute("categories",categoryService.getAllCategories());
        return "index";
    }
    @GetMapping(value = "/descending")
    public String descending(Model model){
        model.addAttribute("tovary",cosmeticsService.getAllCosmetic());
        model.addAttribute("currentUser",getUserData());
        model.addAttribute("categories",categoryService.getAllCategories());
        return "index";
    }

    @GetMapping(value = "/ascending")
    public String ascending(Model model){
        model.addAttribute("tovary",cosmeticsService.getAllProduct());
        model.addAttribute("currentUser",getUserData());
        model.addAttribute("categories",categoryService.getAllCategories());
        return "index";

    }



    @GetMapping(value = "/layout")
    public String layout (Model model,@RequestParam(name = "keyword")String keyword){
        model.addAttribute("currentUser",getUserData());
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("keyword",keyword);
        return "layout/layout";
    }



    @GetMapping(value = "/403")
    public String accessDenied(Model model) {
        return "403";
    }

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("categories",categoryService.getAllCategories());
        return "login";
    }
    @GetMapping(value = "/profile")
    public String profile(Model model){
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("currentUser",getUserData());
        return "profile";
    }
    @GetMapping(value = "/register")
    public String register(Model model){
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("currentUser",getUserData());
        return "register";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable(name = "id")Long id){
        model.addAttribute("tovary",cosmeticsService.getCosmetic(id));
        model.addAttribute("categories",categoryService.getAllCategories());
        return "detail";
    }
    @GetMapping(value = "/edit/{id}")
    public String update(Model model, @PathVariable(name = "id")Long id){
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("tovary",cosmeticsService.getCosmetic(id));
        return "edit";
    }
    @PostMapping(value = "/register")
    public String toRegister(@RequestParam(name = "user_email")String email,
                             @RequestParam(name = "user_password")String password,
                             @RequestParam(name = "re_password")String re_password,
                             @RequestParam(name = "user_fullname")String fullname){
                 if(password.equals(re_password)){
                     Users newUser = new Users();
                     newUser.setFullName(fullname);
                     newUser.setEmail(email);
                     newUser.setPassword(password);

                     if(userService.createUser(newUser)!=null){
                         return "redirect:/register?success";
                     }
                 }
                 return "redirect:/register?error";
    }

    @GetMapping(value = "/categories-for-admin/{id}")
    public String sorted(Model model,@PathVariable(name = "id") Long id){
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("tovary",cosmeticsService.getAllCosmeticsByCategoryId(id));
        return "list";
    }



    private Users getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }

    @GetMapping(value = "/additem")
    public String addItem(Model model){
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("type",typeService.getAllType());
        return "additem";
    }

    @PostMapping(value = "/additem")
    public String AddItems(@RequestParam(name = "item_name")String name,
                           @RequestParam(name = "category_id")Long id,
                           @RequestParam(name = "type_id")Long typeId,
                           @RequestParam(name = "item_description")String description,
                           @RequestParam(name = "item_price") int price,
                           @RequestParam(name = "item_amount")int amount,
                           @RequestParam(name = "productPhoto")MultipartFile file){
                  String productPhoto = uploadPhoto(file);
                Category cat = categoryService.getCategory(id);
                Type type = typeService.getType(typeId);

                if(cat!=null&&type!=null){
                    Cosmetics cosmetic = new Cosmetics();
                    cosmetic.setName(name);
                    cosmetic.setAmount(amount);
                    cosmetic.setDescription(description);
                    cosmetic.setPrice(price);
                    cosmetic.setImage(productPhoto);
                    cosmetic.setType(type);
                    cosmetic.setCategory(cat);

                    cosmeticsService.addCosmetic(cosmetic);
                }
                return "redirect:/";

    }
    @GetMapping(value = "/addtocart/{id}")
    public String addToCart(Model model, @PathVariable(name = "id") Long id) {
        if (session.getAttribute("cart") == null) {
            List<Item> cart = new ArrayList<>();
            cart.add(new Item(null, cosmeticsService.getCosmetic(id), 1));
            session.setAttribute("cart", cart);

        } else {
            List<Item> cart = (List<Item>) session.getAttribute("cart");
            int index = isExist(id, cart);
            if (index == -1) {
                cart.add(new Item(null, cosmeticsService.getCosmetic(id), 1));
            } else {
                int newQty = cart.get(index).getQuantity() + 1;
                cart.get(index).setQuantity(newQty);
            }
            session.setAttribute("cart", cart);

        }
        model.addAttribute("type", typeService.getAllType());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/cart";

    }

    private int isExist(Long id, List<Item> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getCosmetics().getId() == id) {
                return i;
            }
        }
        return -1;
    }




    @GetMapping(value = "/cart")
    public String getCart(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cart", session.getAttribute("cart"));
        int total = 0;
        if (session.getAttribute("cart") != null) {
            List<Item> cart = (List<Item>) session.getAttribute("cart");
            for (Item item : cart) {
                total += item.getCosmetics().getPrice() * item.getQuantity();

            }
        }

        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping(value = "/edit/{id}")
    public String edit(@PathVariable(name = "id")Long id,
                       @RequestParam(name = "item_name")String name,
                       @RequestParam(name = "item_description")String description,
                       @RequestParam(name = "productPhoto")MultipartFile file,
                       @RequestParam(name = "item_price")int price,
                       @RequestParam(name = "item_amount")int amount){


        if(!file.isEmpty()){
            String productPhoto = uploadPhoto(file);
            Cosmetics cosmetic = new Cosmetics();
            cosmetic.setName(name);
            cosmetic.setAmount(amount);
            cosmetic.setDescription(description);
            cosmetic.setPrice(price);
            cosmetic.setImage(productPhoto);
            cosmeticsService.saveCosmetic(cosmetic);
        }

        return "redirect:/";
    }

    @PostMapping(value = "/remove/{id}")
    public String RemoveItem(@PathVariable(name = "id") Long id) {
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        if (cart != null) {
            for (Item crt : cart) {
                if (crt.getCosmetics().getId() == id) {
                    cart.remove(cart.indexOf(crt));
                    break;
                }
            }
        }
        return "redirect:/cart";
    }
    @GetMapping(value = "/delete item/{id}")
    public String DeleteItem(@PathVariable(name = "id") Long id) {
        Cosmetics cosmetics = cosmeticsService.getCosmetic(id);
        if (cosmetics != null) {
            cosmeticsService.deleteCosmetic(cosmetics);
        }
        return "redirect:/";
    }

    @GetMapping(value = "/view-photo/{PhotoName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody
    byte[] viewProductPhoto(@PathVariable(name = "PhotoName") String photoName) throws IOException {
        String photoUrl = viewPath + defaultPath;
        if (photoName != null) {
            photoUrl = viewPath + photoName + ".jpg";

        }
        InputStream inputStream;
        try {
            ClassPathResource resource = new ClassPathResource(photoUrl);
            inputStream = resource.getInputStream();
        } catch (Exception e) {
            ClassPathResource resource = new ClassPathResource(viewPath + defaultPath);
            inputStream = resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(inputStream);
    }

    public String uploadPhoto(MultipartFile file) {
        String photoName = DigestUtils.sha1Hex("photo" + file.getOriginalFilename());
        if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
            try {
                Path path = Paths.get(uploadPath + photoName + ".jpg");
                Path pathTarget = Paths.get(uploadPathTarget + photoName + ".jpg");
                byte[] bytes = file.getBytes();
                Files.write(path, bytes);
                Files.write(pathTarget, bytes);
                return photoName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @GetMapping(value = "/filter-categories/{id}")
    public String filter(Model model,@PathVariable(name = "id")Long id){
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("tovary", cosmeticsService.getAllCosmeticsByCategoryId(id));
        return "index";
    }


//    @PostMapping(value = "/uploadavatar")
//    public String uploadavatar(@RequestParam(name = "admin_ava") MultipartFile file) {
//        if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
//            try {
//                Users currentUser = getUserData();
//                String picName = DigestUtils.sha1Hex("avarat_" + currentUser.getId() + "!Picture");
//                byte bytes[] = file.getBytes();
//                Path path = Paths.get(uploadPath + picName + ".jpg");
//                Files.write(path, bytes);
//                currentUser.userAvatar(picName);
//                userService.saveUser(currentUser);
//                return "redirect:/profile";
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return "redirect:/profile";
//    }


}

