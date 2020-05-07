package webviewselenium.gui.reportChange;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import webviewselenium.gui.ApplicationLoader;
import webviewselenium.gui.generateIssue.GenerateIssueController;

public class SelectCroppedPartController {
    private final static Logger logger = ApplicationLoader.getLogger();
	private final static String NAME_OF_CROPPED_TEMPLATE_IMAGE = "croppedTemplateImage";
    private final static String NAME_OF_CROPPED_SCANNED_IMAGE = "croppedScannedImage";
	
    private static String pathToReportDirectory;
    private static String pathToScannedImage;
    private static String pathToTemplateImage;
    private static String qaBookLink;
    
    private static Integer selectId = 0;
    
    private static RubberBandSelection rubberBandSelection;
    private static ImageView imageView;

    private static Stage stage = new Stage();
    
    /**
    *
    * @param pathToScannedImage relative path to the scanned image that is to be displayed
    * @param pathToTemplateImage relative path to the template image that is to be displayed
    * @param imageIndex index of the image that is to be displayed @param pathToReportDirectory relative path to the report directory
    */
   public SelectCroppedPartController(String pathToScannedImage, String pathToTemplateImage, int imageIndex, String pathToReportDirectory) {
	   int theLatestIndex = pathToTemplateImage.lastIndexOf("ScanDB");
	   qaBookLink = pathToTemplateImage.substring(theLatestIndex + 9, pathToTemplateImage.length() - 6);
	   SelectCroppedPartController.pathToReportDirectory = pathToReportDirectory;
	   SelectCroppedPartController.pathToScannedImage = pathToScannedImage;
	   SelectCroppedPartController.pathToTemplateImage = pathToTemplateImage;
	}
	
	public void showStage() {
		BorderPane root = new BorderPane();
        ScrollPane scrollPane = new ScrollPane();
        Group imageLayer = new Group(); 

        try {
			imageView = new ImageView(new Image(new FileInputStream(pathToScannedImage)));
		} catch (FileNotFoundException ex) {
            ex.printStackTrace();
            logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
		}

        imageLayer.getChildren().add( imageView);
        scrollPane.setContent(imageLayer);
       
        root.setCenter(scrollPane);
        
        rubberBandSelection = new RubberBandSelection(imageLayer);

        ContextMenu contextMenu = new ContextMenu();

        MenuItem cropMenuItem = new MenuItem("Crop");
        cropMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Bounds selectionBounds = rubberBandSelection.getBounds();
                crop(selectionBounds);
            }
        });
        contextMenu.getItems().add(cropMenuItem);
        
        imageLayer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(imageLayer, event.getScreenX(), event.getScreenY());
                }
            }
        });
		stage.setScene(new Scene(root, 1349, 686));
		stage.setResizable(false);
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.setAlwaysOnTop(true);
		stage.show();
	}
	
	private static void crop (Bounds bounds) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");

        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D( bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage( width, height);
        imageView.snapshot(parameters, wi);

        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);

        try {
        	File outputfile = new File(pathToReportDirectory);
            ImageIO.write(bufImageRGB, "png", outputfile);
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
        }
        graphics.dispose();
    }
	
    public static class RubberBandSelection {
        final DragContext dragContext = new DragContext();
        Rectangle rect = new Rectangle();

        Group group;

        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }

        public RubberBandSelection( Group group) {

            this.group = group;

            rect = new Rectangle( 0,0,0,0);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if( event.isSecondaryButtonDown())
                    return;
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove(rect);

                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();

                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().add( rect);
            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if( event.isSecondaryButtonDown())
                    return;

                double offsetX, offsetY;
                
                /*if(event.getX() > imageView.getImage().getWidth()) offsetX = imageView.getImage().getWidth() - dragContext.mouseAnchorX;
                else if(event.getX() < 0) offsetX = dragContext.mouseAnchorX;
                else offsetX = event.getX() - dragContext.mouseAnchorX;
                
                if(event.getY() > imageView.getImage().getHeight()) offsetY = imageView.getImage().getHeight() - dragContext.mouseAnchorY;
                else if(event.getY() < 0) offsetY = dragContext.mouseAnchorY;
                else offsetY = event.getY() - dragContext.mouseAnchorY;*/

                offsetX = event.getX() - dragContext.mouseAnchorX;
                offsetY = event.getY() - dragContext.mouseAnchorY;
                
                if( offsetX > 0) {
                    rect.setWidth( offsetX);
                } else {
                    rect.setX(event.getX());
                    rect.setWidth(dragContext.mouseAnchorX - rect.getX());
                }
                if(offsetY > 0) {
                    rect.setHeight(offsetY);
                } else {
                    rect.setY(event.getY());
                    rect.setHeight(dragContext.mouseAnchorY - rect.getY());
                }
            }
        };

        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if( event.isSecondaryButtonDown()) {
                	return;
                }
                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.TRANSPARENT);
                int startX = (int)rect.getX();
                int widthX = (int)rect.getWidth();
                int imageX = (int)imageView.getImage().getWidth();
                int diffX = widthX;
                if(startX + widthX > imageX)
                {
                    diffX = imageX - startX;
                }
                int startY = (int)rect.getY();
                int widthY = (int)rect.getHeight();
                int imageY = (int)imageView.getImage().getHeight();
                int diffY = widthY;
                if(startY + widthY > imageY)
                {
                    diffY = imageY - startY;
                }
                parameters.setViewport(new Rectangle2D( rect.getX(), rect.getY(), diffX, diffY));

                WritableImage wi = new WritableImage(diffX, diffY);
                imageView.snapshot(parameters, wi);

                BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
                BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

                Graphics2D graphics = bufImageRGB.createGraphics();
                graphics.drawImage(bufImageARGB, 0, 0, null);

                try {
                	File reportDirectory = new File(pathToReportDirectory);
                    if (!reportDirectory.exists()) {
                    	reportDirectory.mkdirs();
                    }
                	File outputfile = new File(pathToReportDirectory + File.separator + "croppedImage" + NAME_OF_CROPPED_SCANNED_IMAGE + "_" + selectId + ".png");
                    ImageIO.write(bufImageRGB, "png", outputfile);
                    
                    BufferedImage img = ImageIO.read(new File(pathToTemplateImage));
                    startX = (int)rect.getX();
                    widthX = (int)rect.getWidth();
                    imageX = (int)imageView.getImage().getWidth();
                    diffX = widthX;
                    if(startX + widthX > imageX)
                    {
                        System.out.println("Hello");
                        diffX = imageX - startX;
                    }
                    startY = (int)rect.getY();
                    widthY = (int)rect.getHeight();
                    imageY = (int)imageView.getImage().getHeight();
                    diffY = widthY;
                    if(startY + widthY > imageY)
                    {
                        diffY = imageY - startY;
                    }

                    //BufferedImage subimage = img.getSubimage((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
                    System.out.println("Start X: " + (int)rect.getX());
                    System.out.println("Width X: " + (int)rect.getWidth());
                    System.out.println("Image X: " + imageView.getImage().getWidth());
                    System.out.println("Diff X: " + diffX);
                    BufferedImage subimage = img.getSubimage((int)rect.getX(), (int)rect.getY(), diffX, diffY);
                    File templateOutputfile = new File(pathToReportDirectory + File.separator + "templateImage" + NAME_OF_CROPPED_TEMPLATE_IMAGE + "_" + selectId + ".png");
                    ImageIO.write(subimage, "png", templateOutputfile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    logger.error(GenerateIssueController.class.getName() + ": " + ex.getMessage());
                }
                graphics.dispose();

                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove(rect);
                
                stage.close();
                
                ReportChangeController reportChangeController = new ReportChangeController(selectId, pathToReportDirectory, qaBookLink);
                reportChangeController.showStage();
                selectId++;
                
            }
        };
        
        private static final class DragContext {
            public double mouseAnchorX;
            public double mouseAnchorY;
        }
    }

}
