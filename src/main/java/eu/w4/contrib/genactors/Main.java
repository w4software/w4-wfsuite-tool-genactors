package eu.w4.contrib.genactors;

import java.io.File;
import java.util.Properties;

import eu.w4.contrib.genactors.cli.CliException;
import eu.w4.contrib.genactors.cli.CliParameter;
import eu.w4.contrib.genactors.cli.CliParser;
import eu.w4.contrib.genactors.cli.CliPrinter;

public class Main {

  public static void main(String[] args) {
    try {
      final Properties props;
      final CliParser parser = new CliParser();

      parser.addParameter(new CliParameter("m", "method (backward compatibility)", "1", false, false));
      parser.addParameter(new CliParameter("s", "server name", "", false, false));
      parser.addParameter(new CliParameter("i", "server instance name", "w4adm", false, false));
      parser.addParameter(new CliParameter("l", "login", "w4adm", false, false));
      parser.addParameter(new CliParameter("w", "passowrd", "w4adm", false, false));
      parser.addParameter(new CliParameter("f", "XLS file", null, false, true));

      parser.addParameter(new CliParameter("a", "policy for actors (CUP)", "CU", false, false));
      parser.addParameter(new CliParameter("r", "policy for roles (CUI)", "CU", false, false));
      parser.addParameter(new CliParameter("d", "policy for domains (CUI)", "CU", false, false));
      parser.addParameter(new CliParameter("aa", "policy for assignments (AU)", "A", false, false));

      try {
        props = parser.parse(args);
      } catch (CliException e) {
        CliPrinter.println("Error: " + e.getMessage());
        CliPrinter.println(parser.getShortHelp());
        System.exit(1);
        return;
      }
      File xlsFile = new File(props.getProperty("f"));

      if (Integer.parseInt(props.getProperty("m")) != 1)
      {
        CliPrinter.println("-m command line parameter is only available for backward compatibility. Value should be 1");
      }
      try {
        String srv = props.getProperty("s");
        String wfs = props.getProperty("i");
        String login = props.getProperty("l");
        String passwd = props.getProperty("w");
        String flagActors = props.getProperty("a").toUpperCase();
        String flagRoles = props.getProperty("r").toUpperCase();
        String flagDomains = props.getProperty("d").toUpperCase();
        String flagAssignations = props.getProperty("aa").toUpperCase();
        GenerateActors ga = new GenerateActors(srv, wfs, login, passwd);
        ga.setDoCreateActors(flagActors.contains("C"));
        ga.setDoUpdateActors(flagActors.contains("U"));
        ga.setDoUpdateActorPasswords(flagActors.contains("P"));

        ga.setDoCreateRoles(flagRoles.contains("C"));
        ga.setDoUpdateRoles(flagRoles.contains("U"));
        ga.setDoIncludeReferencedRoles(flagRoles.contains("I"));

        ga.setDoCreateDomains(flagDomains.contains("C"));
        ga.setDoUpdateDomains(flagDomains.contains("U"));
        ga.setDoIncludeReferencedDomains(flagDomains.contains("D"));

        ga.setDoAssignRoles(flagAssignations.contains("A"));
        ga.setDoUnassignRoles(flagAssignations.contains("U"));

        ga.process(xlsFile, 0);
      } catch (Exception e) {
        e.printStackTrace();
        CliPrinter.println("Error: " + e.getMessage());
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    System.exit(0);
  }

}
