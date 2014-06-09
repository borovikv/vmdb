package md.varoinform.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 6/6/14
 * Time: 1:44 PM
 */
@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name = "EXPORTED_DB.DB_Arc")
public class Arc {
    private Long id;
    // for arc x->y x is tail y is head
    private Node tail;
    private Node head;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "tail_id")
    public Node getTail() {
        return tail;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }

    @ManyToOne
    @JoinColumn(name = "head_id")
    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }
}
