package me.dags.daflight.handler;

import me.dags.daflight.DaFlight;
import me.dags.daflight.MCHooks;
import me.dags.daflight.util.Rotation;
import me.dags.daflight.util.Vector3d;

/**
 * @author dags <dags@dags.me>
 */
public class MovementHandler {

    private static final double SCALE_FACTOR = 1 / Math.sqrt(2);

    private final DaFlight daFlight;

    private float moveForward = 0F;
    private float moveStrafe = 0F;
    private float maxFlySpeed = 10F;
    private float maxWalkSpeed = 10F;

    boolean flying = false;
    boolean sprinting = false;
    boolean flyBoosting = false;
    boolean sprintBoosting = false;

    public MovementHandler(DaFlight daFlight) {
        this.daFlight = daFlight;
    }

    public void applyMovement(Vector3d direction, Rotation rotation) {
        if (flying && !DaFlight.instance().config().disabled) {
            moveFlying(direction, rotation);
        } else if (sprinting && !daFlight.config().disabled) {
            moveSprinting(direction, rotation);
        }
    }

    public void reset() {
        flying = false;
        sprinting = false;
        flyBoosting = false;
        sprintBoosting = false;
        maxFlySpeed = DaFlight.instance().isSinglePlayer() ? 100000F : 1F;
        maxWalkSpeed = DaFlight.instance().isSinglePlayer() ? 100000F : 1F;
    }

    public boolean flying() {
        return flying;
    }

    public boolean sprinting() {
        return sprinting;
    }

    public boolean disableFov() {
        return DaFlight.instance().config().disableFov && flying;
    }

    public boolean disableViewBob() {
        return flying;
    }

    public float jump(float normal) {
        if (sprinting && !daFlight.config().disabled) {
            float boost = sprintBoosting ? daFlight.config().sprintBoost : 1F;
            return normal * Math.min(daFlight.config().sprintSpeed * daFlight.config().jumpModifier * boost, maxWalkSpeed * 5);
        }
        return normal;
    }

    public void setMovementInput(float moveForward, float moveStrafe) {
        this.moveForward = moveForward;
        this.moveStrafe = moveStrafe;
    }

    boolean flyBoosting() {
        return flyBoosting;
    }

    boolean sprintBoosting() {
        return sprintBoosting;
    }

    void setMaxFlySpeed(float maxFlySpeed) {
        this.maxFlySpeed = maxFlySpeed;
    }

    void setMaxWalkSpeed(float maxWalkSpeed) {
        this.maxWalkSpeed = maxWalkSpeed;
    }

    private void moveFlying(Vector3d direction, Rotation rotation) {
        double x = 0;
        double y = 0;
        double z = 0;
        double rads = Math.toRadians(rotation.getYaw());
        double dx = -Math.sin(rads);
        double dz = Math.cos(rads);
        float boost = flyBoosting ? daFlight.config().flyBoost : 1F;

        if (moveForward != 0) {
            x += dx * moveForward * Math.min(daFlight.config().flySpeed * boost, maxFlySpeed);
            z += dz * moveForward * Math.min(daFlight.config().flySpeed * boost, maxFlySpeed);
        }
        if (moveStrafe != 0) {
            x += dz * moveStrafe * Math.min(daFlight.config().flySpeed * daFlight.config().strafeModifier * boost, maxFlySpeed);
            z += dx * -moveStrafe * Math.min(daFlight.config().flySpeed * daFlight.config().strafeModifier * boost, maxFlySpeed);
        }
        if (moveForward != 0 && moveStrafe != 0) {
            x *= SCALE_FACTOR;
            z *= SCALE_FACTOR;
        }
        if (daFlight.config().flight3D && moveForward != 0) {
            y += -rotation.getPitch() * moveForward * (0.9F / 50F) * daFlight.config().verticalModifier * Math.min(daFlight.config().flySpeed * boost, maxFlySpeed);
        }
        if (DaFlight.instance().inputHandler().getFlyUpBind().keyHeld() && MCHooks.Game.inGameHasFocus()) {
            y += Math.min(daFlight.config().flySpeed * boost * daFlight.config().verticalModifier, maxFlySpeed);
        }
        if (DaFlight.instance().inputHandler().getFlyDownBind().keyHeld() && MCHooks.Game.inGameHasFocus()) {
            y -= Math.min(daFlight.config().flySpeed * boost * daFlight.config().verticalModifier, maxFlySpeed);
        }
        direction.update(x, y, z);
    }

    private void moveSprinting(Vector3d direction, Rotation rotation) {
        double x = direction.getX();
        double z = direction.getZ();

        double rads = Math.toRadians(rotation.getYaw());
        double dx = -Math.sin(rads);
        double dz = Math.cos(rads);
        float boost = sprintBoosting ? daFlight.config().sprintBoost : 1F;

        if (moveForward != 0) {
            x += dx * moveForward * Math.min(daFlight.config().sprintSpeed * boost, maxWalkSpeed);
            z += dz * moveForward * Math.min(daFlight.config().sprintSpeed * boost, maxWalkSpeed);
        }
        if (moveStrafe != 0) {
            x += dz * moveStrafe * Math.min(daFlight.config().sprintSpeed * daFlight.config().strafeModifier * boost, maxWalkSpeed);
            z += dx * -moveStrafe * Math.min(daFlight.config().sprintSpeed * daFlight.config().strafeModifier * boost, maxWalkSpeed);
        }
        if (moveForward != 0 && moveStrafe != 0) {
            x *= SCALE_FACTOR;
            z *= SCALE_FACTOR;
        }
        direction.update(x, direction.getY(), z);
    }
}
