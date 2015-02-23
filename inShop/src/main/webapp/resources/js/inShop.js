/**
 * 
 */
$(document).ready(function() {
	$(".tree").treegrid({
		initialState : 'collapsed',
		expanderExpandedClass : 'fa fa-minus-circle',
		expanderCollapsedClass : 'fa fa-plus-circle'
	});
});
$(document).on("click", ".open-CategoryEditDialog", function() {
	var categoryId = $(this).data('id');
	$(".modal-body #categoryId").val(categoryId);
});
