package org.bukkit.event.entity;

import java.util.Map;

import org.bukkit.entity.Entity;

/**
 * Called when an entity is damaged by an entity
 */
public class EntityDamageByEntityEvent extends EntityDamageEvent {
    private final Entity damager;

    @Deprecated
    public EntityDamageByEntityEvent(final Entity damager, final Entity damagee, final DamageCause cause, final int damage) {
        this(damager, damagee, cause, (double) damage);
    }

    @Deprecated
    public EntityDamageByEntityEvent(final Entity damager, final Entity damagee, final DamageCause cause, final double damage) {
        super(damagee, cause, damage);
        this.damager = damager;
    }

    public EntityDamageByEntityEvent(final Entity damager, final Entity damagee, final DamageCause cause, final Map<DamageModifier, Double> modifiers) {
        super(damagee, cause, modifiers);
        this.damager = damager;
    }

    /**
     * Returns the entity that damaged the defender.
     *
     * @return Entity that damaged the defender.
     */
    public Entity getDamager() {
        return damager;
    }
}
