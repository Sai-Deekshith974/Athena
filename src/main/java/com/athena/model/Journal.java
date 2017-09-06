package com.athena.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * Created by Tommy on 2017/8/28.
 */
@Entity
@Table(name = "journal")
@IdClass(JournalPK.class)
public class Journal {
    private String issn;
    private Integer year;
    private Integer index;
    private String title;
    private Integer titleShortPinyin;
    private Integer titlePinyin;
    private Double price;
    private String coverUrl;
    private String directory;
    private Date publishDate;
    private Publisher publisher;

    private List<JournalCopy> copies;

    @Id
    @Column(name = "issn", nullable = false, length = 8)
    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    @Id
    @Column(name = "year", nullable = false)
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Id
    @Column(name = "index", nullable = false)
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Transient
    public void setId(JournalPK journalPK) {
        this.issn = journalPK.getIssn();
        this.year = journalPK.getYear();
        this.index = journalPK.getIndex();
    }

    @Transient
    public JournalPK getId() {
        JournalPK journalPK = new JournalPK();
        journalPK.setIssn(this.issn);
        journalPK.setYear(this.year);
        journalPK.setIndex(this.index);
        return journalPK;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 128)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "title_short_pinyin", nullable = true)
    public Integer getTitleShortPinyin() {
        return titleShortPinyin;
    }

    public void setTitleShortPinyin(Integer titleShortPinyin) {
        this.titleShortPinyin = titleShortPinyin;
    }

    @Basic
    @Column(name = "title_pinyin", nullable = true)
    public Integer getTitlePinyin() {
        return titlePinyin;
    }

    public void setTitlePinyin(Integer titlePinyin) {
        this.titlePinyin = titlePinyin;
    }

    @Basic
    @Column(name = "price", nullable = false, precision = 0)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "cover_url", nullable = true, length = 128)
    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Basic
    @Column(name = "directory", nullable = true, length = -1)
    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @Basic
    @Column(name = "publish_date", nullable = true)
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Journal journal = (Journal) o;

        if (issn != null ? !issn.equals(journal.issn) : journal.issn != null) return false;
        if (year != null ? !year.equals(journal.year) : journal.year != null) return false;
        if (index != null ? !index.equals(journal.index) : journal.index != null) return false;
        if (title != null ? !title.equals(journal.title) : journal.title != null) return false;
        if (titleShortPinyin != null ? !titleShortPinyin.equals(journal.titleShortPinyin) : journal.titleShortPinyin != null)
            return false;
        if (titlePinyin != null ? !titlePinyin.equals(journal.titlePinyin) : journal.titlePinyin != null) return false;
        if (price != null ? !price.equals(journal.price) : journal.price != null) return false;
        if (coverUrl != null ? !coverUrl.equals(journal.coverUrl) : journal.coverUrl != null) return false;
        if (directory != null ? !directory.equals(journal.directory) : journal.directory != null) return false;
        if (publishDate != null ? !publishDate.equals(journal.publishDate) : journal.publishDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = issn != null ? issn.hashCode() : 0;
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (index != null ? index.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (titleShortPinyin != null ? titleShortPinyin.hashCode() : 0);
        result = 31 * result + (titlePinyin != null ? titlePinyin.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (coverUrl != null ? coverUrl.hashCode() : 0);
        result = 31 * result + (directory != null ? directory.hashCode() : 0);
        result = 31 * result + (publishDate != null ? publishDate.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "publisher_id", referencedColumnName = "id", nullable = false)
    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @SuppressWarnings("JpaDataSourceORMInspection")
    @OneToMany
    @JoinTable(name = "journal_copy",
            joinColumns = {@JoinColumn(name = "issn", table = "journal", referencedColumnName = "issn"),
                    @JoinColumn(name = "year", table = "journal", referencedColumnName = "year"),
                    @JoinColumn(name = "index", table = "journal", referencedColumnName = "index")},
            inverseJoinColumns = @JoinColumn(name="copy_id", referencedColumnName = "id",table = "copy")

    )
    public List<JournalCopy> getCopies() {
        return copies;
    }

    public void setCopies(List<JournalCopy> copies) {
        this.copies = copies;
    }
}