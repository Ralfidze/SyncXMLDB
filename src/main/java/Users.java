import java.util.List;

 class Users {
    private int id;
    private String depCode;
    private String depJob;
    private String description;

    public Users(String depCode, String depJob, String description) {
        this.depCode = depCode;
        this.depJob = depJob;
        this.description = description;
    }
    public int getId() {
        return id;
    }
    public String getDepCode() {
        return depCode;
    }
    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }
    public String getDepJob() {
        return depJob;
    }
    public void setDepJob(String depJob) {
        this.depJob = depJob;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public int hashCode(){
        return this.depCode.hashCode()*this.depCode.hashCode();
    }
    @Override
    public boolean equals(Object o){
        if(o == null)
            return false;
        if(o==this)
            return true;
        if(!(getClass()==o.getClass()))
            return false;
        else {
            Users u=(Users)o;
            if(u.depCode.equals(this.depCode) && u.depJob.equals(this.depJob))
                return true;
            else
                return false;
        }
    }
}



