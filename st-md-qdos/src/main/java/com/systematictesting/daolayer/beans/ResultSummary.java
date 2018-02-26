package com.systematictesting.daolayer.beans;

public class ResultSummary {

	private Long passed;
	private Long failed;
	private Long manual;
	private Long aborted;
	private Long total;
	
	public Long getPassed() {
		return passed;
	}
	public void setPassed(Long passed) {
		this.passed = passed;
	}
	public Long getFailed() {
		return failed;
	}
	public void setFailed(Long failed) {
		this.failed = failed;
	}
	public Long getAborted() {
		return aborted;
	}
	public void setAborted(Long aborted) {
		this.aborted = aborted;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Long getManual() {
		return manual;
	}
	public void setManual(Long manual) {
		this.manual = manual;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aborted == null) ? 0 : aborted.hashCode());
		result = prime * result + ((failed == null) ? 0 : failed.hashCode());
		result = prime * result + ((manual == null) ? 0 : manual.hashCode());
		result = prime * result + ((passed == null) ? 0 : passed.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultSummary other = (ResultSummary) obj;
		if (aborted == null) {
			if (other.aborted != null)
				return false;
		} else if (!aborted.equals(other.aborted))
			return false;
		if (failed == null) {
			if (other.failed != null)
				return false;
		} else if (!failed.equals(other.failed))
			return false;
		if (manual == null) {
			if (other.manual != null)
				return false;
		} else if (!manual.equals(other.manual))
			return false;
		if (passed == null) {
			if (other.passed != null)
				return false;
		} else if (!passed.equals(other.passed))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ResultSummary [passed=" + passed + ", failed=" + failed + ", manual=" + manual + ", aborted=" + aborted
				+ ", total=" + total + "]";
	}
	
}
