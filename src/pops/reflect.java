package pops;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class reflect {
  public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
    //    PopUpModel pop = new PopUpModel();
    //    Field[] declaredFields = pop.getClass().getDeclaredFields();
    //
    //    for (Field field : declaredFields) {
    //      System.out.println(field.getName());
    //    }
    //    Method[] declaredMethods = pop.getClass().getDeclaredMethods();
    //    for (Method method : declaredMethods) {
    //      if (method.getName().equals("cal")) {
    //
    //        method.invoke(pop, 1, 3);
    //      }
    //    }
    //      Optional<PopUpModel> poppp = pop("code");
    //    System.out.println(poppp.get().getDescription());
//    for (int i = 1; i <=10; i++) {
//      for (int j = 1; j <= 10; j++) {
//        System.out.println(i*j);
//      }
//      // System.out.println("outer loop");
//    }
    String[] friends = new String[4];
    friends[0]="samuel";
    friends[1]="kagichu";
    friends[2]="muriithi";
    friends[3]="Leon";

    Optional<String> first = Arrays.stream(friends).findFirst();

    Arrays.stream(friends).distinct().collect(Collectors.toList());
    System.out.println(first.get());
  }

  public static Optional<PopUpModel> pop(String code) {
    PopUpModel pop = new PopUpModel("description", code);
    return Optional.of(pop);
  }
}
