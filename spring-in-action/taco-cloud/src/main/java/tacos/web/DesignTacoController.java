package tacos.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

import tacos.Taco;
import tacos.Ingredient;
import tacos.Order;
import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

  private final IngredientRepository ingredientRepo;
  private final TacoRepository designRepo;

  @Autowired
  public DesignTacoController(
      IngredientRepository ingredientRepo,
      TacoRepository designRepo) {
    this.ingredientRepo = ingredientRepo;
    this.designRepo = designRepo;
  }

  @ModelAttribute(name="order")
  public Order order() {
    return new Order();
  }

  @ModelAttribute(name="design")
  public Taco taco() {
    return new Taco();
  }

  @GetMapping
  public String showDesignForm(Model model) {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredientRepo.findAll().forEach(i -> ingredients.add(i));

    if (!model.containsAttribute("design")) {
      model.addAttribute("design", new Taco());
    }
    else {
      Taco design = (Taco) model.asMap().get("design");
      markIngredientsSelected(ingredients, design.getIngredients());
    }

    Type[] types = Ingredient.Type.values();
    for (Type type : types) {
      model.addAttribute(type.toString().toLowerCase(), 
        filterByType(ingredients, type));
    }

    return "design";
  }

  @PostMapping
  public String processDesign(
      RedirectAttributes redirectAttributes, 
      @ModelAttribute(name="design") @Valid Taco design, BindingResult bindingResult,
      @ModelAttribute Order order) {
    
    if (bindingResult.hasErrors()) {
      ObjectError e = bindingResult.getAllErrors().get(0);
      log.error(e.toString());
      
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.design", bindingResult);
      redirectAttributes.addFlashAttribute("design", design);
      return "redirect:/design";
    }

    Taco saved = designRepo.save(design);
    order.addDesign(saved);

    return "redirect:/orders/current";
  }

  private static List<Ingredient> filterByType(List<Ingredient> allIngredients, Type targetType) {
    return allIngredients.stream()
      .filter(ingredient -> targetType.equals(ingredient.getType()))
      .collect(Collectors.toList());
  }

  private static void markIngredientsSelected(List<Ingredient> allIngredients, List<Ingredient> selectedIngredientList) {
    if (allIngredients == null || selectedIngredientList == null) {
      return;
    }
    List<String> selectedIngredientIdList = selectedIngredientList.stream().map(ingredient -> ingredient.getId()).collect(Collectors.toList());
    Set<String> selectedIngredientIds = new HashSet<>(selectedIngredientIdList);
    allIngredients.forEach(ingredient -> ingredient.setSelected(selectedIngredientIds.contains(ingredient.getId())));
  }
}
