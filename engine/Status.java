package engine;
public abstract class Status<T> {
	protected T status;
	public Status() {}
	public Status(T status) {
		set_status(status);
	}
	public void set_status(T status) {
		this.status = status;
		update_by_status();
	}
	public T get_status(){
		return status;
	}
	protected abstract void update_by_status();
}