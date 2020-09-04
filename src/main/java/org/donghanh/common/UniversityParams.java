package org.donghanh.common;

public final class UniversityParams {
  public final String code;
  public final String name;
  public final Constants.Foundation foundation;
  public final String studentClass;
  public final String evaluatedBy;
  public final String logo;
  public final double vnCoef;
  public final int nbJuriesByCopy;
  public final int nbJuries;

  public Constants.Foundation foundation() {
    return foundation;
  }

  public String foundationName() {
    return foundation.getFullName();
  }


  public String studentClass() {
    return studentClass;
  }

  public String evaluatedBy() {
    return evaluatedBy;
  }

  public String logo() {
    return logo;
  }

  public double vnCoef() {
    return vnCoef;
  }

  public int nbJuriesByCopy() {
    return nbJuriesByCopy;
  }

  public int nbJuries() {
    return nbJuries;
  }

  private UniversityParams(Builder builder) {
    this.code = builder.code;
    this.name = builder.name;
    this.evaluatedBy = builder.evaluatedBy;
    this.studentClass = builder.studentClass;
    this.logo = builder.logo;
    this.foundation = builder.foundation;
    this.vnCoef = builder.vnCoef;
    this.nbJuries = builder.nbJuries;
    this.nbJuriesByCopy = builder.nbJuriesByCopy;
  }

  public String code() {
    return code;
  }

  public String name() {
    return name;
  }

  public static class Builder {
    private String code = "";
    private String name = "";
    private Constants.Foundation foundation;
    private String studentClass = "";
    private String evaluatedBy = "";
    private String logo = "";
    private double vnCoef = 0;
    private int nbJuriesByCopy = 0;
    private int nbJuries = 0;

    public Builder() {
    }

    public Builder code(String code) {
      this.code = code;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder vnCoef(double vnCoef) {
      this.vnCoef = vnCoef;
      return this;
    }

    public Builder nbJuriesByCopy(int nbJuriesPerCopy) {
      this.nbJuriesByCopy = nbJuriesPerCopy;
      return this;
    }

    public Builder nbJuries(int nbJuries) {
      this.nbJuries = nbJuries;
      return this;
    }

    public Builder studentClass(String studentClass) {
      this.studentClass = studentClass;
      return this;
    }

    public Builder evaluatedBy(String evaluatedBy) {
      this.evaluatedBy = evaluatedBy;
      return this;
    }

    public Builder logo(String logo) {
      this.logo = logo;
      return this;
    }

    public Builder foundation(Constants.Foundation foundation) {
      this.foundation = foundation;
      return this;
    }

    public UniversityParams build() {
      return new UniversityParams(this);
    }
  }

}
