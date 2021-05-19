package com.amplifyframework.datastore.generated.model;


import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the TaskModelAmp type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "TaskModelAmps")
@Index(name = "byTeam", fields = {"teamID","title","description"})
public final class TaskModelAmp implements Model {
  public static final QueryField ID = field("TaskModelAmp", "id");
  public static final QueryField TEAM_ID = field("TaskModelAmp", "teamID");
  public static final QueryField TITLE = field("TaskModelAmp", "title");
  public static final QueryField DESCRIPTION = field("TaskModelAmp", "description");
  public static final QueryField S3_STORAGE_ID = field("TaskModelAmp", "s3StorageId");
  public static final QueryField LOCATION_CREATION = field("TaskModelAmp", "locationCreation");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String teamID;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String") String description;
  private  @ModelField(targetType="String") String s3StorageId;
  private final @ModelField(targetType="String") String locationCreation;
  public String getId() {
      return id;
  }
  
  public String getTeamId() {
      return teamID;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getDescription() {
      return description;
  }
  
  public String getS3StorageId() {
      return s3StorageId;
  }
  
  public String getLocationCreation() {
      return locationCreation;
  }
  public void setS3StorageId(String s3StorageId){this.s3StorageId=s3StorageId;}

  private TaskModelAmp(String id, String teamID, String title, String description, String s3StorageId, String locationCreation) {
    this.id = id;
    this.teamID = teamID;
    this.title = title;
    this.description = description;
    this.s3StorageId = s3StorageId;
    this.locationCreation = locationCreation;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      TaskModelAmp taskModelAmp = (TaskModelAmp) obj;
      return ObjectsCompat.equals(getId(), taskModelAmp.getId()) &&
              ObjectsCompat.equals(getTeamId(), taskModelAmp.getTeamId()) &&
              ObjectsCompat.equals(getTitle(), taskModelAmp.getTitle()) &&
              ObjectsCompat.equals(getDescription(), taskModelAmp.getDescription()) &&
              ObjectsCompat.equals(getS3StorageId(), taskModelAmp.getS3StorageId()) &&
              ObjectsCompat.equals(getLocationCreation(), taskModelAmp.getLocationCreation());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTeamId())
      .append(getTitle())
      .append(getDescription())
      .append(getS3StorageId())
      .append(getLocationCreation())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("TaskModelAmp {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("teamID=" + String.valueOf(getTeamId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("s3StorageId=" + String.valueOf(getS3StorageId()) + ", ")
      .append("locationCreation=" + String.valueOf(getLocationCreation()))
      .append("}")
      .toString();
  }
  
  public static TeamIdStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static TaskModelAmp justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new TaskModelAmp(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      teamID,
      title,
      description,
      s3StorageId,
      locationCreation);
  }
  public interface TeamIdStep {
    TitleStep teamId(String teamId);
  }
  

  public interface TitleStep {
    BuildStep title(String title);
  }
  

  public interface BuildStep {
    TaskModelAmp build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep description(String description);
    BuildStep s3StorageId(String s3StorageId);
    BuildStep locationCreation(String locationCreation);
  }
  

  public static class Builder implements TeamIdStep, TitleStep, BuildStep {
    private String id;
    private String teamID;
    private String title;
    private String description;
    private String s3StorageId;
    private String locationCreation;
    @Override
     public TaskModelAmp build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new TaskModelAmp(
          id,
          teamID,
          title,
          description,
          s3StorageId,
          locationCreation);
    }
    
    @Override
     public TitleStep teamId(String teamId) {
        Objects.requireNonNull(teamId);
        this.teamID = teamId;
        return this;
    }
    
    @Override
     public BuildStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
     public BuildStep s3StorageId(String s3StorageId) {
        this.s3StorageId = s3StorageId;
        return this;
    }
    
    @Override
     public BuildStep locationCreation(String locationCreation) {
        this.locationCreation = locationCreation;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String teamId, String title, String description, String s3StorageId, String locationCreation) {
      super.id(id);
      super.teamId(teamId)
        .title(title)
        .description(description)
        .s3StorageId(s3StorageId)
        .locationCreation(locationCreation);
    }
    
    @Override
     public CopyOfBuilder teamId(String teamId) {
      return (CopyOfBuilder) super.teamId(teamId);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder s3StorageId(String s3StorageId) {
      return (CopyOfBuilder) super.s3StorageId(s3StorageId);
    }
    
    @Override
     public CopyOfBuilder locationCreation(String locationCreation) {
      return (CopyOfBuilder) super.locationCreation(locationCreation);
    }
  }
  
}
