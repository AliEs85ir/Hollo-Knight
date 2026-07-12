package ir.Ali.hollowknightme.controller.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import java.util.ArrayList;
import java.util.List;

public class CompositeContactListener implements ContactListener {

    private final List<ContactListener> listeners = new ArrayList<>();

    public void addListener(ContactListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void beginContact(Contact contact) {
        listeners.forEach(l -> l.beginContact(contact));
    }

    @Override
    public void endContact(Contact contact) {
        listeners.forEach(l -> l.endContact(contact));
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        listeners.forEach(l -> l.preSolve(contact, oldManifold));
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        listeners.forEach(l -> l.postSolve(contact, impulse));
    }
}
