package edu.kit.kastel.lissa.sketches.coco

import edu.kit.kastel.lissa.sketches.coco.domain.COCOAnnotation
import edu.kit.kastel.lissa.sketches.coco.domain.COCOData
import edu.kit.kastel.lissa.sketches.model.Sketch
import edu.kit.kastel.lissa.sketches.model.elements.class_diagram.AssociationType
import edu.kit.kastel.lissa.sketches.model.elements.class_diagram.IAssociation
import edu.kit.kastel.lissa.sketches.model.elements.class_diagram.IClass
import edu.kit.kastel.lissa.sketches.model.impl.BoxImpl
import edu.kit.kastel.lissa.sketches.model.impl.RelationImpl

class UMLDataAccessor(private val data: COCOData) {
    companion object {
        const val CLASS_CATEGORY = "ClassNode"
        val ASSOCIATIONS = listOf(
            "Aggregation",
            "Composition",
            "Extension",
            "Dependency",
            "Realization",
            "AssociationUnidirectional",
            "AssociationBidirectional"
        )
        const val LABEL_CATEGORY = "Label"
    }

    private val sketch: Sketch = Sketch()

    init {
        createSketchRepresentation()
    }

    fun sketch() = sketch

    private fun createSketchRepresentation() {
        addClasses()
        addAssociations()
    }

    private fun addAssociations() {
        val associations = ASSOCIATIONS.flatMap { c -> findByCategory(c) }
        associations.forEach { createAssociation(it) }
    }

    private fun addClasses() {
        val classes = findByCategory(CLASS_CATEGORY)
        val labels = findByCategory(LABEL_CATEGORY)
        classes.forEach { createClass(it, labels) }
    }

    private fun createAssociation(association: COCOAnnotation) {
        val element1 = findClassById(association.arrowStart!!)
        val element2 = findClassById(association.arrowEnd!!)
        val relation = RelationImpl(association.umlId, 1.0)
        relation.addToAssociation(element1)
        relation.addToAssociation(element2)
        sketch.addSketchElement(relation)
        val umlAssociation = sketch.changeInterpretation(relation, IAssociation::class)
        umlAssociation.setAssociationType(AssociationType.UNKNOWN)
    }

    private fun findClassById(id: Int): IClass {
        val classes = findByCategory(CLASS_CATEGORY)
        val clazz = classes.find { c -> c.id == id }!!
        return sketch.getBoxElements(IClass::class).find { c -> c.id() == clazz.umlId }!!
    }

    private fun createClass(clazz: COCOAnnotation, labels: List<COCOAnnotation>) {
        val name = labels.find { l -> l.belongsTo == clazz.id && l.labelType == "name" }?.name
        val box = BoxImpl(name ?: clazz.umlId, 1.0)
        sketch.addSketchElement(box)
        val umlClass = sketch.changeInterpretation(box, IClass::class)
        umlClass.setId(clazz.umlId)
    }

    private fun findByCategory(category: String): List<COCOAnnotation> {
        val classCategory = data.categories.find { c -> c.name == category }!!
        return data.annotations.filter { a -> a.categoryId == classCategory.id }
    }
}
