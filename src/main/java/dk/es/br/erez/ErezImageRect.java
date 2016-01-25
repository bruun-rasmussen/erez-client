package dk.es.br.erez;

/**
 *
 * @author osa
 */
public interface ErezImageRect {
    float getTopOffsetFraction();     // 0.0f..bottom, default 0.0f
    float getLeftOffsetFraction();    // 0.0f..right,  default 0.0f
    float getBottomOffsetFraction();  // top..1.0f,    default 1.0f
    float getRightOffsetFraction();   // left..1.0f,   default 1.0f
    float getImageRotation();         // 0.0f..360.0f  default 0.0f

    public final static ErezImageRect DEFAULT = new ErezImageRect() {
        public float getTopOffsetFraction() { return 0.0f; }
        public float getLeftOffsetFraction() { return 0.0f; }
        public float getBottomOffsetFraction() { return 1.0f; }
        public float getRightOffsetFraction() { return 1.0f; }
        public float getImageRotation() { return 0.0f; }
    };
}
