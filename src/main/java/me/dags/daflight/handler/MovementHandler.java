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
            return normal * clamp(daFlight.config().sprintSpeed * daFlight.config().jumpModifier * boost, maxWalkSpeed * 5);
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
        double x, y, z;
        double yaw = Math.toRadians(rotation.getYaw());
        double pitch = Math.toRadians(rotation.getPitch());
        float boost = flyBoosting ? daFlight.config().flyBoost : 1F;

        if (daFlight.config().flight3D) {
            x = moveForward * -Math.sin(yaw) * Math.abs(Math.cos(pitch));
            y = moveForward * -Math.sin(pitch);
            z = moveForward * Math.cos(yaw) * Math.abs(Math.cos(pitch));
        } else {
            x = moveForward * -Math.sin(yaw);
            y = 0;
            z = moveForward * Math.cos(yaw);
        }

        x += moveStrafe * Math.cos(yaw);
        z += moveStrafe * Math.sin(yaw);

        if (DaFlight.instance().inputHandler().getFlyUpBind().keyHeld() && MCHooks.Game.inGameHasFocus()) {
            y += 1;
        }

        if (DaFlight.instance().inputHandler().getFlyDownBind().keyHeld() && MCHooks.Game.inGameHasFocus()) {
            y -= 1;
        }

        direction.update(x, y, z);
        direction.normalize();
        direction.mult(clamp(daFlight.config().flySpeed * boost, maxFlySpeed));
    }

    private void moveSprinting(Vector3d direction, Rotation rotation) {
        double x = direction.getX();
        double z = direction.getZ();

        double rads = Math.toRadians(rotation.getYaw());
        double dx = -Math.sin(rads);
        double dz = Math.cos(rads);
        float boost = sprintBoosting ? daFlight.config().sprintBoost : 1F;

        if (moveForward != 0) {
            x += dx * moveForward * clamp(daFlight.config().sprintSpeed * boost, maxWalkSpeed);
            z += dz * moveForward * clamp(daFlight.config().sprintSpeed * boost, maxWalkSpeed);
        }
        if (moveStrafe != 0) {
            x += dz * moveStrafe * clamp(daFlight.config().sprintSpeed * daFlight.config().strafeModifier * boost, maxWalkSpeed);
            z += dx * -moveStrafe * clamp(daFlight.config().sprintSpeed * daFlight.config().strafeModifier * boost, maxWalkSpeed);
        }
        if (moveForward != 0 && moveStrafe != 0) {
            x *= SCALE_FACTOR;
            z *= SCALE_FACTOR;
        }
        direction.update(x, direction.getY(), z);
    }

    private static float clamp(float in, float max) {
        return in < max ? in : max;
    }
}
