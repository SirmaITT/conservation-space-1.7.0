'use strict';

let ModelManagementSandbox = require('../../model-management.js').ModelManagementSandbox;

describe('Models management general section - browsing', () => {

  let general;
  let modelData;
  let sandbox;

  function openPage(systemLang, userLang, modelId) {
    sandbox = new ModelManagementSandbox();
    sandbox.open(userLang, systemLang, modelId);

    modelData = sandbox.getModelData();
    general = modelData.getGeneralSection();
  }

  it('should show correct headers for the entity class & definition when a definition is selected', () => {
    openPage('en', 'bg', 'EO1001');
    expect(general.getClassName()).to.eventually.eq('Entity');
    expect(general.getClassIdentifier()).to.eventually.eq('(http://www.ontotext.com/proton/protontop#Entity)');

    expect(general.getDefinitionName()).to.eventually.eq('entity');
    expect(general.getDefinitionIdentifier()).to.eventually.eq('(EO1001)');
  });

  it('should show correct headers for the entity class & definition when a definition is selected in another language', () => {
    openPage('bg', 'de', 'EO1001');
    expect(general.getClassName()).to.eventually.eq('Обект');
    expect(general.getClassIdentifier()).to.eventually.eq('(http://www.ontotext.com/proton/protontop#Entity)');

    expect(general.getDefinitionName()).to.eventually.eq('обект');
    expect(general.getDefinitionIdentifier()).to.eventually.eq('(EO1001)');
  });

  it('should show correct headers for the entity class when a class is selected', () => {
    openPage('en', 'bg', 'http://www.ontotext.com/proton/protontop#Entity');
    expect(general.getClassName()).to.eventually.eq('Entity');
    expect(general.getClassIdentifier()).to.eventually.eq('(http://www.ontotext.com/proton/protontop#Entity)');
  });

  it('should show correct headers for the entity class when a class is selected in another language', () => {
    openPage('bg', 'de', 'http://www.ontotext.com/proton/protontop#Entity');
    expect(general.getClassName()).to.eventually.eq('Обект');
    expect(general.getClassIdentifier()).to.eventually.eq('(http://www.ontotext.com/proton/protontop#Entity)');
  });

  it('should show correct attributes for the class', () => {
    openPage('en', 'bg', 'http://www.ontotext.com/proton/protontop#Entity');
    general.getClassAttribute('http://purl.org/dc/terms/title')
      .then(attr => expect(attr.getLabel()).to.eventually.eq('Title'));
    general.getClassAttribute('http://purl.org/dc/terms/description')
      .then(attr => expect(attr.getLabel()).to.eventually.eq('Description'));
  });

  it('should show correct attributes for the definition', () => {
    openPage('en', 'bg', 'EO1001');
    general.getDefinitionAttribute('identifier')
      .then(attr => expect(attr.getLabel()).to.eventually.eq('Identifier'));
    general.getDefinitionAttribute('label')
      .then(attr => expect(attr.getLabel()).to.eventually.eq('Label'));
    general.getDefinitionAttribute('abstract')
      .then(attr => expect(attr.getLabel()).to.eventually.eq('Is abstract'));
  });

  it('should show correct attributes for the class in another language', () => {
    openPage('bg', 'de', 'http://www.ontotext.com/proton/protontop#Entity');
    general.getClassAttribute('http://purl.org/dc/terms/title')
      .then(attr => expect(attr.getLabel()).to.eventually.eq('Заглавие'));
    general.getClassAttribute('http://purl.org/dc/terms/description')
      .then(attr => expect(attr.getLabel()).to.eventually.eq('Описание'));
  });

  it('should show correct attributes for the definition  in another language', () => {
    openPage('bg', 'de', 'EO1001');
    general.getDefinitionAttribute('identifier')
      .then(attr => expect(attr.getLabel()).to.eventually.eq('Идентификатор'));
    general.getDefinitionAttribute('label')
      .then(attr => expect(attr.getLabel()).to.eventually.eq('Име'));
    general.getDefinitionAttribute('abstract')
      .then(attr => expect(attr.getLabel()).to.eventually.eq('Абстрактна'));
  });

  it('should show correct tooltips for attributes', () => {
    openPage('en', 'bg', 'EO1001');
    general.getDefinitionAttribute('identifier')
      .then(attr => {
        expect(attr.hasTooltip()).to.eventually.be.true;
        expect(attr.getTooltip()).to.eventually.equal('This is tooltip for Identifier');
      });
    general.getDefinitionAttribute('abstract')
      .then(attr => {
        expect(attr.hasTooltip()).to.eventually.be.true;
        expect(attr.getTooltip()).to.eventually.equal('This is tooltip for Is abstract');
      });
    general.getDefinitionAttribute('label')
      .then(attr => {
        expect(attr.hasTooltip()).to.eventually.be.false;
      });
  });

  it('should show correct tooltips for attributes in another language', () => {
    openPage('bg', 'de', 'EO1001');
    general.getDefinitionAttribute('identifier')
      .then(attr => {
        expect(attr.hasTooltip()).to.eventually.be.true;
        expect(attr.getTooltip()).to.eventually.equal('Това е подсказка за Идентификатор');
      });
    general.getDefinitionAttribute('abstract')
      .then(attr => {
        expect(attr.hasTooltip()).to.eventually.be.true;
        expect(attr.getTooltip()).to.eventually.equal('Това е подсказка за Абстрактна');
      });
    general.getDefinitionAttribute('label')
      .then(attr => {
        expect(attr.hasTooltip()).to.eventually.be.false;
      });
  });

  it('should show correct attribute values for the class', () => {
    openPage('en', 'bg', 'http://www.ontotext.com/proton/protontop#Entity');
    general.getClassAttribute('http://purl.org/dc/terms/title')
      .then(attr => expect(attr.getField().getValue()).to.eventually.eq('Entity'));
    general.getClassAttribute('http://purl.org/dc/terms/description')
      .then(attr => expect(attr.getField().getValue()).to.eventually.eq('An abstract entity which can represent everything'));
  });

  it('should show correct attribute values for the class in another language', () => {
    openPage('bg', 'de', 'http://www.ontotext.com/proton/protontop#Entity');
    general.getClassAttribute('http://purl.org/dc/terms/title')
      .then(attr => expect(attr.getField().getValue()).to.eventually.eq('Обект'));
    general.getClassAttribute('http://purl.org/dc/terms/description')
      .then(attr => expect(attr.getField().getValue()).to.eventually.eq('Абстрактен обект който може да представя всичко'));
  });

  it('should show correct attribute values for the definition', () => {
    openPage('en', 'bg', 'EO1001');
    general.getDefinitionAttribute('identifier')
      .then(attr => expect(attr.getField().getValue()).to.eventually.eq('EO1001'));
    general.getDefinitionAttribute('label')
      .then(attr => expect(attr.getField().getValue()).to.eventually.eq('entity'));
    general.getDefinitionAttribute('abstract')
      .then(attr => expect(attr.getField().isChecked()).to.eventually.eq('true'));
  });

  it('should show correct attribute values for the definition in another language', () => {
    openPage('bg', 'de', 'EO1001');
    general.getDefinitionAttribute('identifier')
      .then(attr => expect(attr.getField().getValue()).to.eventually.eq('EO1001'));
    general.getDefinitionAttribute('label')
      .then(attr => expect(attr.getField().getValue()).to.eventually.eq('обект'));
    general.getDefinitionAttribute('abstract')
      .then(attr => expect(attr.getField().isChecked()).to.eventually.eq('true'));
  });

  it('should open a dialog listing all available values for multi language attributes for class', () => {
    openPage('en', 'bg', 'http://www.ontotext.com/proton/protontop#Entity');
    general.getClassAttribute('http://purl.org/dc/terms/title')
      .then(attr => {
        let dialog = attr.getValuesDialog();
        expect(dialog.getTitleText()).to.eventually.equal('Title');

        // languages directly provided from the model data
        expect(dialog.getLabel(2)).to.eventually.eq('English');
        expect(dialog.getLabel(0)).to.eventually.eq('Bulgarian');

        // artificially inserted based on system languages
        expect(dialog.getLabel(1)).to.eventually.eq('German');
        expect(dialog.getLabel(3)).to.eventually.eq('fi');
        expect(dialog.getLabel(4)).to.eventually.eq('Romanian');

        // values which are directly provided from the model data
        expect(dialog.getField(2).getValue()).to.eventually.eq('Entity');
        expect(dialog.getField(0).getValue()).to.eventually.eq('Обект');

        // values should be empty since no data is provided for them
        expect(dialog.getField(1).getValue()).to.eventually.eq('');
        expect(dialog.getField(3).getValue()).to.eventually.eq('');
        expect(dialog.getField(4).getValue()).to.eventually.eq('');
      });
  });

  it('should open a dialog listing all available values for multi language attributes for definition', () => {
    openPage('en', 'bg', 'EO1001');
    general.getDefinitionAttribute('label')
      .then(attr => {
        let dialog = attr.getValuesDialog();
        expect(dialog.getTitleText()).to.eventually.equal('Label');

        // languages directly provided from the model data
        expect(dialog.getLabel(2)).to.eventually.eq('English');
        expect(dialog.getLabel(0)).to.eventually.eq('Bulgarian');

        // artificially inserted based on system languages
        expect(dialog.getLabel(1)).to.eventually.eq('German');
        expect(dialog.getLabel(3)).to.eventually.eq('fi');
        expect(dialog.getLabel(4)).to.eventually.eq('Romanian');

        // values which are directly provided from the model data
        expect(dialog.getField(2).getValue()).to.eventually.eq('entity');
        expect(dialog.getField(0).getValue()).to.eventually.eq('обект');

        // values should be empty since no data is provided for them
        expect(dialog.getField(1).getValue()).to.eventually.eq('');
        expect(dialog.getField(3).getValue()).to.eventually.eq('');
        expect(dialog.getField(4).getValue()).to.eventually.eq('');
      });
  });

  it('should open a dialog listing all available languages and their translations', () => {
    openPage('bg', 'en', 'EO1001');
    general.getDefinitionAttribute('label')
      .then(attr => {
        let dialog = attr.getValuesDialog();

        // languages directly provided from the model data
        expect(dialog.getLabel(2)).to.eventually.eq('Англииски');
        expect(dialog.getLabel(0)).to.eventually.eq('Български');

        // artificially inserted based on system languages
        expect(dialog.getLabel(1)).to.eventually.eq('Немски');
        expect(dialog.getLabel(3)).to.eventually.eq('fi');

        // should fallback when translation for is not present
        expect(dialog.getLabel(4)).to.eventually.eq('Romanian');
      });
  });
});