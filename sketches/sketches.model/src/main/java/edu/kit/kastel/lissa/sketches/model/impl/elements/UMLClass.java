package edu.kit.kastel.lissa.sketches.model.impl.elements;

import edu.kit.kastel.lissa.sketches.model.SketchElementType;
import edu.kit.kastel.lissa.sketches.model.elements.IUMLClass;
import edu.kit.kastel.lissa.sketches.model.impl.BoxElement;

public class UMLClass extends UMLThing implements IUMLClass {
    private static final long serialVersionUID = 3310996720085779684L;

    public static final String ATTRIBUTE_KEY = "cls_attributes";
    public static final String METHOD_KEY = "cls_methods";

    public UMLClass(BoxElement element) {
        super(element, ATTRIBUTE_KEY, METHOD_KEY);
    }

    @Override
    public SketchElementType getCurrentInterpretation() {
        return SketchElementType.CLASS;
    }

}
