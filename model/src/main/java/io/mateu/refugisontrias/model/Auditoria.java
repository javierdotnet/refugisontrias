package io.mateu.refugisontrias.model;

import io.mateu.ui.mdd.server.interfaces.AuditRecord;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by miguel on 19/1/17.
 */
@Embeddable
@Getter@Setter
public class Auditoria implements AuditRecord {

    @ManyToOne
    private Usuario createdBy;

    private LocalDateTime created = LocalDateTime.now();

    @ManyToOne
    private Usuario modifiedBy;

    private LocalDateTime modified;


    @Override
    public String toString() {

        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-mm-dd hh:MM:ss");

        String s = "";
        String s1 = "";
        if (getCreatedBy() != null) s1 += "by " + getCreatedBy().getLogin();
        if (getCreated() != null) s1 += (("".equals(s1))?"":" ") + getCreated().format(f);
        String s2 = "";
        if (getModifiedBy() != null) s2 += "by " + getModifiedBy().getLogin();
        if (getModified() != null) s2 += (("".equals(s1))?"":" ") + getModified().format(f);

        if (!"".equals(s1)) s += "Created " + s1;
        if (!"".equals(s2)){
            if ("".equals(s)) s += "Modified ";
            else s += ", modified ";
            s += s2;
        }

        return s;
    }

    @Override
    public void touch(EntityManager em, String login) {
        if (login != null && !"".equals(login)) {
            Usuario u = em.find(Usuario.class, login);
            setModifiedBy(u);
            if (getCreatedBy() == null) setCreatedBy(u);
        }
        if (getCreated() == null) setCreated(LocalDateTime.now());
        setModified(LocalDateTime.now());
    }

    public void touch(Usuario user) {
        setModifiedBy(user);
        if (getCreatedBy() == null) setCreatedBy(user);

        if (getCreated() == null) setCreated(LocalDateTime.now());
        setModified(LocalDateTime.now());
    }
}
