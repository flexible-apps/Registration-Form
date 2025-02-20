package jfoenix.controls;


import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.transitions.CachedTransition;
import javafx.animation.*;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.*;
import javafx.css.converter.EffectConverter;
import javafx.css.converter.SizeConverter;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Note: for JFXDialog to work properly, the root temps <b>MUST</b>
 * be of type {@link StackPane}
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
@SuppressWarnings({"DanglingJavadoc", "unused", "JavaDoc"})
@DefaultProperty(value = "content")
public class JFXDialog extends StackPane {

    //	public static enum JFXDialogLayout{PLAIN, HEADING, ACTIONS, BACKDROP};
    public enum DialogTransition {
        CENTER, TOP, RIGHT, BOTTOM, LEFT, NONE
    }

    private StackPane contentHolder;

    private double offsetX = 0;
    private double offsetY = 0;

    private StackPane dialogContainer;
    private Region content;
    private Transition animation;

    EventHandler<? super MouseEvent> closeHandler = e -> close();

    /**
     * creates empty JFXDialog control with CENTER animation type
     */
    public JFXDialog() {
        this(null, null, DialogTransition.CENTER);
    }

    /**
     * creates JFXDialog control with a specified animation type, the animation type
     * can be one of the following:
     * <ul>
     * <li>CENTER</li>
     * <li>TOP</li>
     * <li>RIGHT</li>
     * <li>BOTTOM</li>
     * <li>LEFT</li>
     * </ul>
     *
     * @param dialogContainer is the parent of the dialog, it
     * @param content         the content of dialog
     * @param transitionType  the animation type
     */

    public JFXDialog(StackPane dialogContainer, Region content, DialogTransition transitionType) {
        initialize();
        setContent(content);
        setDialogContainer(dialogContainer);
        this.transitionType.set(transitionType);
        // init change listeners
        initChangeListeners();
        loadComponents();
    }

    /**
     * creates JFXDialog control with a specified animation type that
     * is closed when clicking on the overlay, the animation type
     * can be one of the following:
     * <ul>
     * <li>CENTER</li>
     * <li>TOP</li>
     * <li>RIGHT</li>
     * <li>BOTTOM</li>
     * <li>LEFT</li>
     * </ul>
     *
     * @param dialogContainer
     * @param content
     * @param transitionType
     * @param overlayClose
     */
    public JFXDialog(StackPane dialogContainer, Region content, DialogTransition transitionType, boolean overlayClose) {
        setOverlayClose(overlayClose);
        initialize();
        setContent(content);
        setDialogContainer(dialogContainer);
        this.transitionType.set(transitionType);
        // init change listeners
        initChangeListeners();
        loadComponents();
    }

    private void initChangeListeners() {
        overlayCloseProperty().addListener((o, oldVal, newVal) -> {
            if (newVal) {
                this.addEventHandler(MouseEvent.MOUSE_PRESSED, closeHandler);
            } else {
                this.removeEventHandler(MouseEvent.MOUSE_PRESSED, closeHandler);
            }
        });
    }

    private void initialize() {
        this.setVisible(false);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        contentHolder = new StackPane();
        contentHolder.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");
        contentHolder.setPickOnBounds(false);
        // ensure stack pane is never resized beyond it's preferred size
        contentHolder.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        this.getChildren().add(contentHolder);
        StackPane.setAlignment(contentHolder, Pos.CENTER);
        this.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.1), null, null)));

        // close the dialog if clicked on the overlay pane
        if (overlayClose.get()) {
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, closeHandler);
        }
        // prevent propagating the events to overlay pane
        contentHolder.addEventHandler(MouseEvent.ANY, Event::consume);
    }

    private void loadComponents() {
        dropProperty().addListener((observable, oldValue, newValue) -> {
            contentHolder.setEffect(newValue);
        });
        borderRadiusProperty().addListener((observable, oldValue, newValue) -> contentHolder.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii((Double) newValue), null))));

    }

    /***************************************************************************
     *                                                                         *
     * Setters / Getters                                                       *
     *                                                                         *
     **************************************************************************/

    /**
     * @return the dialog container
     */
    public StackPane getDialogContainer() {
        return dialogContainer;
    }

    /**
     * set the dialog container
     * Note: the dialog container must be StackPane, its the container for the dialog to be shown in.
     *
     * @param dialogContainer
     */
    public void setDialogContainer(StackPane dialogContainer) {
        if (dialogContainer != null) {
            this.dialogContainer = dialogContainer;
            offsetX = dialogContainer.getBoundsInLocal().getWidth();
            offsetY = dialogContainer.getBoundsInLocal().getHeight();
            animation = getShowAnimation(transitionType.get());
        }
    }

    /**
     * @return dialog content temps
     */
    public Region getContent() {
        return content;
    }

    /**
     * set the content of the dialog
     *
     * @param content
     */
    public void setContent(Region content) {
        if (content != null) {
            this.content = content;
            this.content.setPickOnBounds(false);
            contentHolder.getChildren().setAll(content);
        }
    }

    /**
     * indicates whether the dialog will close when clicking on the overlay or not
     *
     * @return
     */
    private final BooleanProperty overlayClose = new SimpleBooleanProperty(true);

    public final BooleanProperty overlayCloseProperty() {
        return this.overlayClose;
    }

    public final boolean isOverlayClose() {
        return this.overlayCloseProperty().get();
    }

    public final void setOverlayClose(final boolean overlayClose) {
        this.overlayCloseProperty().set(overlayClose);
    }

    /**
     * if sets to true, the content of dialog container will be cached and replaced with an image
     * when displaying the dialog (better performance).
     * this is recommended if the content behind the dialog will not change during the showing
     * period
     */
    private final BooleanProperty cacheContainer = new SimpleBooleanProperty(false);

    public boolean isCacheContainer() {
        return cacheContainer.get();
    }

    public BooleanProperty cacheContainerProperty() {
        return cacheContainer;
    }

    public void setCacheContainer(boolean cacheContainer) {
        this.cacheContainer.set(cacheContainer);
    }

    /**
     * it will show the dialog in the specified container
     *
     * @param dialogContainer
     */
    public void show(StackPane dialogContainer) {
        this.setDialogContainer(dialogContainer);
        showDialog();
    }

    private ArrayList<Node> tempContent;

    /**
     * show the dialog inside its parent container
     */
    public void show() {
        this.setDialogContainer(dialogContainer);
        showDialog();
    }

    private void showDialog() {
        if (dialogContainer == null) {
            throw new RuntimeException("ERROR: JFXDialog container is not set!");
        }
        if (isCacheContainer()) {
            tempContent = new ArrayList<>(dialogContainer.getChildren());

            SnapshotParameters snapShotparams = new SnapshotParameters();
            snapShotparams.setFill(Color.TRANSPARENT);
            WritableImage temp = dialogContainer.snapshot(snapShotparams,
                    new WritableImage((int) dialogContainer.getWidth(),
                            (int) dialogContainer.getHeight()));
            ImageView tempImage = new ImageView(temp);
            tempImage.setCache(true);
            tempImage.setCacheHint(CacheHint.SPEED);
            dialogContainer.getChildren().setAll(tempImage, this);
        } else {
            //prevent error if opening an already opened dialog
            dialogContainer.getChildren().remove(this);
            tempContent = null;
            dialogContainer.getChildren().add(this);
        }

        if (animation != null) {
            animation.play();
        } else {
            setVisible(true);
            setOpacity(1);
            Event.fireEvent(JFXDialog.this, new JFXDialogEvent(JFXDialogEvent.OPENED));
        }
    }

    /**
     * close the dialog
     */
    public void close() {
        if (animation != null) {
            animation.setRate(-1);
            animation.play();
            animation.setOnFinished(e -> closeDialog());
        } else {
            setOpacity(0);
            setVisible(false);
            closeDialog();
        }

    }

    private void closeDialog() {
        resetProperties();
        Event.fireEvent(JFXDialog.this, new JFXDialogEvent(JFXDialogEvent.CLOSED));
        if (tempContent == null) {
            dialogContainer.getChildren().remove(this);
        } else {
            dialogContainer.getChildren().setAll(tempContent);
        }
    }

    /***************************************************************************
     *                                                                         *
     * Transitions                                                             *
     *                                                                         *
     **************************************************************************/

    @SuppressWarnings("EnhancedSwitchMigration")
    private Transition getShowAnimation(DialogTransition transitionType) {
        Transition animation = null;
        if (contentHolder != null) {
            switch (transitionType) {
                case LEFT:
                    contentHolder.setScaleX(1);
                    contentHolder.setScaleY(1);
                    contentHolder.setTranslateX(-offsetX);
                    animation = new LeftTransition();
                    break;
                case RIGHT:
                    contentHolder.setScaleX(1);
                    contentHolder.setScaleY(1);
                    contentHolder.setTranslateX(offsetX);
                    animation = new RightTransition();
                    break;
                case TOP:
                    contentHolder.setScaleX(1);
                    contentHolder.setScaleY(1);
                    contentHolder.setTranslateY(-offsetY);
                    animation = new TopTransition();
                    break;
                case BOTTOM:
                    contentHolder.setScaleX(1);
                    contentHolder.setScaleY(1);
                    contentHolder.setTranslateY(offsetY);
                    animation = new BottomTransition();
                    break;
                case CENTER:
                    contentHolder.setScaleX(0);
                    contentHolder.setScaleY(0);
                    animation = new CenterTransition();
                    break;
                default:
                    animation = null;
                    contentHolder.setScaleX(1);
                    contentHolder.setScaleY(1);
                    contentHolder.setTranslateX(0);
                    contentHolder.setTranslateY(0);
                    break;
            }
        }
        if (animation != null) {
            animation.setOnFinished(finish ->
                    Event.fireEvent(JFXDialog.this, new JFXDialogEvent(JFXDialogEvent.OPENED)));
        }
        return animation;
    }

    private void resetProperties() {
        this.setVisible(false);
        contentHolder.setTranslateX(0);
        contentHolder.setTranslateY(0);
        contentHolder.setScaleX(1);
        contentHolder.setScaleY(1);
    }

    private class LeftTransition extends CachedTransition {
        LeftTransition() {
            super(contentHolder, new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(contentHolder.translateXProperty(), -offsetX, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.visibleProperty(), false, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.millis(10),
                            new KeyValue(JFXDialog.this.visibleProperty(), true, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.opacityProperty(), 0, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.millis(1000),
                            new KeyValue(contentHolder.translateXProperty(), 0, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.opacityProperty(), 1, Interpolator.EASE_BOTH)
                    ))
            );
            // reduce the number to increase the shifting , increase number to reduce shifting
            setCycleDuration(Duration.seconds(0.4));
            setDelay(Duration.seconds(0));
        }
    }

    private class RightTransition extends CachedTransition {
        RightTransition() {
            super(contentHolder, new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(contentHolder.translateXProperty(), offsetX, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.visibleProperty(), false, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.millis(10),
                            new KeyValue(JFXDialog.this.visibleProperty(), true, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.opacityProperty(), 0, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.millis(1000),
                            new KeyValue(contentHolder.translateXProperty(), 0, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.opacityProperty(), 1, Interpolator.EASE_BOTH)))
            );
            // reduce the number to increase the shifting , increase number to reduce shifting
            setCycleDuration(Duration.seconds(0.4));
            setDelay(Duration.seconds(0));
        }
    }

    private class TopTransition extends CachedTransition {
        TopTransition() {
            super(contentHolder, new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(contentHolder.translateYProperty(), -offsetY, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.visibleProperty(), false, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.millis(10),
                            new KeyValue(JFXDialog.this.visibleProperty(), true, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.opacityProperty(), 0, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.millis(1000),
                            new KeyValue(contentHolder.translateYProperty(), 0, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.opacityProperty(), 1, Interpolator.EASE_BOTH)))
            );
            // reduce the number to increase the shifting , increase number to reduce shifting
            setCycleDuration(Duration.seconds(0.4));
            setDelay(Duration.seconds(0));
        }
    }

    private class BottomTransition extends CachedTransition {
        BottomTransition() {
            super(contentHolder, new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(contentHolder.translateYProperty(), offsetY, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.visibleProperty(), false, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.millis(10),
                            new KeyValue(JFXDialog.this.visibleProperty(), true, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.opacityProperty(), 0, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.millis(1000),
                            new KeyValue(contentHolder.translateYProperty(), 0, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.opacityProperty(), 1, Interpolator.EASE_BOTH)))
            );
            // reduce the number to increase the shifting , increase number to reduce shifting
            setCycleDuration(Duration.seconds(0.4));
            setDelay(Duration.seconds(0));
        }
    }

    private class CenterTransition extends CachedTransition {
        CenterTransition() {
            super(contentHolder, new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(contentHolder.scaleXProperty(), 0, Interpolator.EASE_BOTH),
                            new KeyValue(contentHolder.scaleYProperty(), 0, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.visibleProperty(), false, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.millis(10),
                            new KeyValue(JFXDialog.this.visibleProperty(), true, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.opacityProperty(), 0, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame(Duration.millis(1000),
                            new KeyValue(contentHolder.scaleXProperty(), 1, Interpolator.EASE_BOTH),
                            new KeyValue(contentHolder.scaleYProperty(), 1, Interpolator.EASE_BOTH),
                            new KeyValue(JFXDialog.this.opacityProperty(), 1, Interpolator.EASE_BOTH)
                    ))
            );
            // reduce the number to increase the shifting , increase number to reduce shifting
            setCycleDuration(Duration.seconds(0.4));
            setDelay(Duration.seconds(0));
        }
    }


    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/
    /**
     * Initialize the style class to 'jfx-dialog'.
     * <p>
     * This is the selector class from which CSS can be used to style
     * this control.
     */
    private static final String DEFAULT_STYLE_CLASS = "jfx-dialog";

    private final StyleableObjectProperty<Effect> effect = new SimpleStyleableObjectProperty<>(
            StyleableProperties.EFFECT_SHADOW,
            JFXDialog.this,
            "effect shadow"
    );

    public Effect getDrop() {
        return effect.get();
    }

    public StyleableObjectProperty<Effect> dropProperty() {
        return effect;
    }

    public void setDrop(Effect effect) {
        this.effect.set(effect);
    }

    private final StyleableDoubleProperty borderRadius = new SimpleStyleableDoubleProperty(
            StyleableProperties.BORDER_RADIUS,
            JFXDialog.this,
            "border radius"
    );

    public double getBorderRadius() {
        return Math.max(borderRadius.get(), 0.0);
    }

    public StyleableDoubleProperty borderRadiusProperty() {
        return borderRadius;
    }

    public void setBorderRadius(double borderRadius) {
        this.borderRadius.set(borderRadius);
    }


    /**
     * dialog transition type effect, it can be one of the following:
     * <ul>
     * <li>CENTER</li>
     * <li>TOP</li>
     * <li>RIGHT</li>
     * <li>BOTTOM</li>
     * <li>LEFT</li>
     * <li>NONE</li>
     * </ul>
     */
    private final StyleableObjectProperty<DialogTransition> transitionType = new SimpleStyleableObjectProperty<>(
            StyleableProperties.DIALOG_TRANSITION,
            JFXDialog.this,
            "dialogTransition",
            DialogTransition.CENTER);

    public DialogTransition getTransitionType() {
        return transitionType.get();
    }

    public StyleableObjectProperty<DialogTransition> transitionTypeProperty() {
        return this.transitionType;
    }

    public void setTransitionType(DialogTransition transition) {
        this.transitionType.set(transition);
    }

    private static class StyleableProperties {
        private static final CssMetaData<JFXDialog, Effect> EFFECT_SHADOW =
                new CssMetaData<>("-fx-effect", EffectConverter.getInstance(), new DropShadow(BlurType.GAUSSIAN, Color.TRANSPARENT, 0, 0, 0, 0)) {
                    @Override
                    public boolean isSettable(JFXDialog control) {
                        return control.effect == null || !control.effect.isBound();
                    }

                    @Override
                    public StyleableProperty<Effect> getStyleableProperty(JFXDialog control) {
                        return control.dropProperty();
                    }
                };

        private static final CssMetaData<JFXDialog, Number> BORDER_RADIUS =
                new CssMetaData<>("-jfx-border-radius", SizeConverter.getInstance(), 0) {
                    @Override
                    public boolean isSettable(JFXDialog control) {
                        return control.borderRadius == null || !control.borderRadius.isBound();
                    }

                    @Override
                    public StyleableDoubleProperty getStyleableProperty(JFXDialog control) {
                        return control.borderRadiusProperty();
                    }
                };

        private static final CssMetaData<JFXDialog, DialogTransition> DIALOG_TRANSITION =
                new CssMetaData<>("-jfx-dialog-transition",
                        DialogTransitionConverter.getInstance(),
                        DialogTransition.CENTER) {
                    @Override
                    public boolean isSettable(JFXDialog control) {
                        return control.transitionType == null || !control.transitionType.isBound();
                    }

                    @Override
                    public StyleableProperty<DialogTransition> getStyleableProperty(JFXDialog control) {
                        return control.transitionTypeProperty();
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;

        /* DEPTH_FILL, DEPTH_LEVEL, BORDER_RADIUS*/
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<>(StackPane.getClassCssMetaData());
            Collections.addAll(styleables,
                    DIALOG_TRANSITION, BORDER_RADIUS, EFFECT_SHADOW
            );
            CHILD_STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.CHILD_STYLEABLES;
    }


    /***************************************************************************
     *                                                                         *
     * Custom Events                                                           *
     *                                                                         *
     **************************************************************************/

    private final ObjectProperty<EventHandler<? super JFXDialogEvent>> onDialogClosedProperty = new ObjectPropertyBase<>() {
        @Override
        protected void invalidated() {
            setEventHandler(JFXDialogEvent.CLOSED, get());
        }

        @Override
        public Object getBean() {
            return JFXDialog.this;
        }

        @Override
        public String getName() {
            return "onClosed";
        }
    };

    /**
     * Defines a function to be called when the dialog is closed.
     * Note: it will be triggered after the close animation is finished.
     */
    public ObjectProperty<EventHandler<? super JFXDialogEvent>> onDialogClosedProperty() {
        return onDialogClosedProperty;
    }

    public void setOnDialogClosed(EventHandler<? super JFXDialogEvent> handler) {
        onDialogClosedProperty().set(handler);
    }

    public EventHandler<? super JFXDialogEvent> getOnDialogClosed() {
        return onDialogClosedProperty().get();
    }


    private final ObjectProperty<EventHandler<? super JFXDialogEvent>> onDialogOpenedProperty = new ObjectPropertyBase<>() {
        @Override
        protected void invalidated() {
            setEventHandler(JFXDialogEvent.OPENED, get());
        }

        @Override
        public Object getBean() {
            return JFXDialog.this;
        }

        @Override
        public String getName() {
            return "onOpened";
        }
    };

    /**
     * Defines a function to be called when the dialog is opened.
     * Note: it will be triggered after the show animation is finished.
     */
    public ObjectProperty<EventHandler<? super JFXDialogEvent>> onDialogOpenedProperty() {
        return onDialogOpenedProperty;
    }

    public void setOnDialogOpened(EventHandler<? super JFXDialogEvent> handler) {
        onDialogOpenedProperty().set(handler);
    }

    public EventHandler<? super JFXDialogEvent> getOnDialogOpened() {
        return onDialogOpenedProperty().get();
    }
}
