package eu.w4.contrib.genactors;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.BeanMap;

import fr.w4.buildtime.dynamic.TWFdomain;
import fr.w4.buildtime.dynamic.TWFrole;

public final class Helper {

  private Helper() {
    // prevent instanciation
  }

  private static final Set<String> WF_SET;
  static {
    WF_SET = new HashSet<String>();
    WF_SET.addAll(new BeanMap(new TWFfullActor()).keySet());
    WF_SET.addAll(new BeanMap(new TWFrole()).keySet());
    WF_SET.addAll(new BeanMap(new TWFdomain()).keySet());
  }

  /**
   * Search exact W4 field name from free text string
   *
   * @param name
   * @return
   */
  public static String findField(String name) {
    // clean up string
    name = name.replaceAll("[ _]", "");

    if (name.equalsIgnoreCase("TYPE")) return "TYPE";

    for (final String key : WF_SET) {
      if (name.equalsIgnoreCase(key) || (name+"REF").equalsIgnoreCase(key)) {
        return key;
      }
      if (key.equalsIgnoreCase("STR") && name.equalsIgnoreCase("NAME")) {
        return key;
      }
    }
    throw new RuntimeException("Property '" + name + "' is not known by W4-Engine");
  }

}
