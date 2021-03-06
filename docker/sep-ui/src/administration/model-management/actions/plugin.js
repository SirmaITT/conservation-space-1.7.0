PluginRegistry.add('actions', [{
  name: 'manageModelAction',
  module: 'administration/model-management/actions/manage/manage-model-action'
}]);

PluginRegistry.add('model-action-factories', [{
  order: 10,
  action: 'ModelChangeAttributeAction',
  name: 'modelChangeAttributeActionFactory',
  module: 'administration/model-management/actions/change/model-change-attribute-action-factory'
}, {
  order: 20,
  action: 'ModelValidateAttributesAction',
  name: 'modelValidateAttributesActionFactory',
  module: 'administration/model-management/actions/state/model-validate-attributes-action-factory'
}, {
  order: 30,
  action: 'ModelValidateAttributeAction',
  name: 'modelValidateAttributeActionFactory',
  module: 'administration/model-management/actions/state/model-validate-attribute-action-factory'
}, {
  order: 40,
  action: 'ModelRestoreInheritedAttributeAction',
  name: 'modelRestoreInheritedAttributeActionFactory',
  module: 'administration/model-management/actions/restore/model-restore-inherited-attribute-action-factory'
}, {
  order: 50,
  action: 'ModelRestoreInheritedFieldAction',
  name: 'modelRestoreInheritedFieldActionFactory',
  module: 'administration/model-management/actions/restore/model-restore-inherited-field-action-factory'
}, {
  order: 60,
  action: 'ModelRestoreInheritedControlAction',
  name: 'modelRestoreInheritedControlActionFactory',
  module: 'administration/model-management/actions/restore/model-restore-inherited-control-action-factory'
}, {
  order: 70,
  action: 'ModelRestoreInheritedRegionAction',
  name: 'modelRestoreInheritedRegionActionFactory',
  module: 'administration/model-management/actions/restore/model-restore-inherited-region-action-factory'
}, {
  order: 80,
  action: 'ModelRestoreInheritedHeaderAction',
  name: 'modelRestoreInheritedHeaderActionFactory',
  module: 'administration/model-management/actions/restore/model-restore-inherited-header-action-factory'
}, {
  order: 90,
  action: 'ModelCreateFieldAction',
  name: 'modelCreateFieldActionFactory',
  module: 'administration/model-management/actions/create/model-create-field-action-factory'
}, {
  order: 100,
  action: 'ModelCreatePropertyAction',
  name: 'modelCreatePropertyActionFactory',
  module: 'administration/model-management/actions/create/model-create-property-action-factory'
}, {
  order: 110,
  action: 'ModelCreateControlAction',
  name: 'modelCreateControlActionFactory',
  module: 'administration/model-management/actions/create/model-create-control-action-factory'
}, {
  order: 120,
  action: 'ModelCreateControlParamAction',
  name: 'modelCreateControlParamActionFactory',
  module: 'administration/model-management/actions/create/model-create-control-param-action-factory'
}, {
  order: 130,
  action: 'ModelRemoveControlAction',
  name: 'modelRemoveControlActionFactory',
  module: 'administration/model-management/actions/remove/model-remove-control-action-factory'
}]);

PluginRegistry.add('model-action-processors', [{
  order: 10,
  action: 'ModelChangeAttributeAction',
  name: 'modelChangeAttributeActionProcessor',
  module: 'administration/model-management/actions/change/model-change-attribute-action-processor'
}, {
  order: 20,
  action: 'ModelValidateAttributesAction',
  name: 'modelValidateAttributesActionProcessor',
  module: 'administration/model-management/actions/state/model-validate-attributes-action-processor'
}, {
  order: 30,
  action: 'ModelValidateAttributeAction',
  name: 'modelValidateAttributeActionProcessor',
  module: 'administration/model-management/actions/state/model-validate-attribute-action-processor'
}, {
  order: 40,
  action: 'ModelRestoreInheritedAttributeAction',
  name: 'modelRestoreInheritedAttributeActionProcessor',
  module: 'administration/model-management/actions/restore/model-restore-inherited-attribute-action-processor'
}, {
  order: 50,
  action: 'ModelRestoreInheritedFieldAction',
  name: 'modelRestoreInheritedFieldActionProcessor',
  module: 'administration/model-management/actions/restore/model-restore-inherited-field-action-processor'
}, {
  order: 60,
  action: 'ModelRestoreInheritedControlAction',
  name: 'modelRestoreInheritedControlActionProcessor',
  module: 'administration/model-management/actions/restore/model-restore-inherited-control-action-processor'
}, {
  order: 70,
  action: 'ModelRestoreInheritedRegionAction',
  name: 'modelRestoreInheritedRegionActionProcessor',
  module: 'administration/model-management/actions/restore/model-restore-inherited-region-action-processor'
}, {
  order: 80,
  action: 'ModelRestoreInheritedHeaderAction',
  name: 'modelRestoreInheritedHeaderActionProcessor',
  module: 'administration/model-management/actions/restore/model-restore-inherited-header-action-processor'
}, {
  order: 90,
  action: 'ModelCreateFieldAction',
  name: 'modelCreateFieldActionProcessor',
  module: 'administration/model-management/actions/create/model-create-field-action-processor'
}, {
  order: 100,
  action: 'ModelCreatePropertyAction',
  name: 'modelCreatePropertyActionProcessor',
  module: 'administration/model-management/actions/create/model-create-property-action-processor'
}, {
  order: 110,
  action: 'ModelCreateControlAction',
  name: 'modelCreateControlActionProcessor',
  module: 'administration/model-management/actions/create/model-create-control-action-processor'
}, {
  order: 120,
  action: 'ModelCreateControlParamAction',
  name: 'modelCreateControlParamActionProcessor',
  module: 'administration/model-management/actions/create/model-create-control-param-action-processor'
}, {
  order: 130,
  action: 'ModelRemoveControlAction',
  name: 'modelRemoveControlActionProcessor',
  module: 'administration/model-management/actions/remove/model-remove-control-action-processor'
}]);
