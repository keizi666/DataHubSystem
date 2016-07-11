/*
 * Data Hub Service (DHuS) - For Space data distribution.
 * Copyright (C) 2013,2014,2015 GAEL Systems
 *
 * This file is part of DHuS software sources.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.gael.dhus.database.liquibase;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.DatabaseException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveDuplicateRoles implements CustomTaskChange
{
   private static Logger LOGGER = Logger.getLogger (RemoveDuplicateRoles.class);

   @Override
   public void execute (Database database) throws CustomChangeException
   {
      String searchDuplicate = "SELECT USER_ID, ROLES, count(ROLES) " +
            "FROM USER_ROLES " +
            "GROUP BY USER_ID, ROLES " +
            "HAVING count(ROLES) > 1";

      String deleteDuplicate =
            "DELETE FROM USER_ROLES WHERE USER_ID=%d AND ROLES='%s'";

      String resetRole = "INSERT INTO USER_ROLES VALUES(%d, '%s')";

      try
      {
         JdbcConnection jdbc = (JdbcConnection) database.getConnection ();
         PreparedStatement getDuplicate = jdbc.prepareStatement (
               searchDuplicate);
         ResultSet result = getDuplicate.executeQuery ();

         while (result.next ())
         {
            int u = result.getInt (1);
            String r = result.getString (2);

            PreparedStatement delete =
                  jdbc.prepareStatement (String.format (deleteDuplicate, u, r));
            delete.executeUpdate ();

            PreparedStatement reset =
                  jdbc.prepareStatement (String.format (resetRole, u, r));
            reset.executeUpdate ();
         }
         result.close ();
         getDuplicate.close ();
      }
      catch (DatabaseException | SQLException e)
      {
         LOGGER.error ("An error occurred during removeDuplicationRoles", e);
      }
   }

   @Override
   public String getConfirmationMessage ()
   {
      return null;
   }

   @Override
   public void setUp () throws SetupException
   {
   }

   @Override
   public void setFileOpener (ResourceAccessor resourceAccessor)
   {
   }

   @Override
   public ValidationErrors validate (Database database)
   {
      return null;
   }
}