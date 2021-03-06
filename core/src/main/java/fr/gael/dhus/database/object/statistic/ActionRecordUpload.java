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
package fr.gael.dhus.database.object.statistic;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table (name = "ACTION_RECORD_UPLOADS")
public class ActionRecordUpload extends ActionRecord implements Serializable
{
   private static final long serialVersionUID = -257656398449198251L;

   /**
    * Notification (case of upload)
    */
   @Column (name = "NOTIFICATION", columnDefinition = "BOOLEAN",
            nullable = true)
   private boolean notificationRequired;

   /**
    * Product identifier. This field is a simple String (and not a Product since
    * products can be removed from the database.
    */
   @Column (name = "PRODUCT_IDENTIFIER", nullable = false)
   private String productIdentifier;

   /**
    * Product size.
    */
   @Column (name = "PRODUCT_SIZE", nullable = true)
   private Long productSize;

   /**
    * Collection list.
    */
   @ElementCollection (targetClass=String.class)
   @Cascade(value={CascadeType.ALL})
   @CollectionTable (name = "COLLECTION_ID_NAME",
                     joinColumns = @JoinColumn (name="ACTION_RECORD_UPLOAD_ID"))
   private Set<String> collectionNameList = new HashSet<String> ();

   public boolean isNotificationRequired ()
   {
      return notificationRequired;
   }

   public void setNotificationRequired (boolean notification)
   {
      this.notificationRequired = notification;
   }

   public String getProductIdentifier ()
   {
      return productIdentifier;
   }

   public void setProductIdentifier (String product_identifier)
   {
      this.productIdentifier = product_identifier;
   }

   public Long getProductSize ()
   {
      return productSize;
   }

   public void setProductSize (Long product_size)
   {
      this.productSize = product_size;
   }

   public Set<String> getCollectionNameList ()
   {
      return collectionNameList;
   }

   public void setCollectionNameList (Set<String> collection_name_list)
   {
      this.collectionNameList = collection_name_list;
   }
}
