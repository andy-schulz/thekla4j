(function () {
    'use strict';

    function escapeHtml(value) {
        return String(value || '')
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;');
    }

    function hasText(value) {
        return value !== null && value !== undefined && String(value).trim().length > 0;
    }

    function normalizeRequirement(entry) {
        if (!entry) {
            return null;
        }
        var name = hasText(entry.name) ? String(entry.name).trim() : '';
        var url = hasText(entry.url) ? String(entry.url).trim() : '';
        if (!name && !url) {
            return null;
        }
        return { name: name || url, url: url };
    }

    function collectFromExtra(extra) {
        if (!extra || !Array.isArray(extra.requirements)) {
            return [];
        }
        return extra.requirements.map(normalizeRequirement).filter(Boolean);
    }

    function collectFromLinks(links) {
        if (!Array.isArray(links)) {
            return [];
        }
        return links
            .filter(function (link) {
                return link && String(link.type || '').toLowerCase() === 'requirement';
            })
            .map(normalizeRequirement)
            .filter(Boolean);
    }

    function uniqueRequirements(requirements) {
        var seen = Object.create(null);
        return requirements.filter(function (item) {
            var key = item.name + '\0' + item.url;
            if (seen[key]) {
                return false;
            }
            seen[key] = true;
            return true;
        });
    }

    function extractRequirements(testResult) {
        var fromExtra = collectFromExtra(testResult.extra);
        if (fromExtra.length) {
            return uniqueRequirements(fromExtra);
        }
        return uniqueRequirements(collectFromLinks(testResult.links));
    }

    var RequirementsBlockView = Backbone.Marionette.View.extend({
        className: 'pane__section',
        templateContext: function () {
            return { requirements: this.options.requirements || [] };
        },
        template: function (data) {
            if (!data.requirements.length) {
                return '';
            }
            var items = data.requirements.map(function (item) {
                var icon = '<i class="fa fa-file-text"></i> ';
                if (hasText(item.url)) {
                    return '<li class="requirements__item"><a class="link" href="'
                        + escapeHtml(item.url) + '" target="_blank" rel="noopener noreferrer">'
                        + icon + escapeHtml(item.name) + '</a></li>';
                }
                return '<li class="requirements__item"><span>'
                    + icon + escapeHtml(item.name) + '</span></li>';
            }).join('');

            return '<h3 class="pane__section-title">Requirements</h3>'
                + '<ul class="requirements__list">' + items + '</ul>';
        }
    });

    var RequirementsContainerView = Backbone.Marionette.View.extend({
        regions: { content: '.requirements-view' },
        initialize: function () {
            this.requirements = extractRequirements(this.model.toJSON());
        },
        template: function () {
            return '<div class="requirements-view"></div>';
        },
        onRender: function () {
            if (!this.requirements.length) {
                return;
            }
            this.showChildView('content', new RequirementsBlockView({ requirements: this.requirements }));
        }
    });

    allure.api.addTestResultBlock(RequirementsContainerView, { position: 'before' });
})();
