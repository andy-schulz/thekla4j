var toggler = document.querySelectorAll(".task");
var inToggler = document.querySelectorAll(".label.inContentButton");
var outToggler = document.querySelectorAll(".label.outContentButton");
var attachmentToggler = document.querySelectorAll(".label.attachmentContentButton");
var videoToggler = document.querySelectorAll(".label.videoContentButton");
var descToggler = document.querySelectorAll(".ellipses");
var i;

for (i = 0; i < toggler.length; i++) {
  toggler[i].addEventListener("click", function() {
    this.parentElement.querySelector(".nested").classList.toggle("active");
    this.classList.toggle("task-open");
  });
}

for (i = 0; i < inToggler.length; i++) {
    inToggler[i].addEventListener("click", function() {
        this.parentElement.querySelector(".inInfo").classList.toggle("inActive");
        this.classList.toggle("active");
    });
}

for (i = 0; i < outToggler.length; i++) {
    outToggler[i].addEventListener("click", function() {
        this.parentElement.querySelector(".outInfo").classList.toggle("outActive");
        this.classList.toggle("active");
    });
}

for (i = 0; i < descToggler.length; i++) {
    descToggler[i].addEventListener("click", function() {
        this.parentElement.querySelector(".longDescription").classList.toggle("descriptionActive");
        this.classList.toggle("active");
    });
}

for (i = 0; i < attachmentToggler.length; i++) {
    attachmentToggler[i].addEventListener("click", function() {
        this.parentElement.querySelector(".attachmentInfo").classList.toggle("attachmentActive");
        this.classList.toggle("active");
    });
}

for (i = 0; i < videoToggler.length; i++) {
    videoToggler[i].addEventListener("click", function() {
        this.parentElement.querySelector(".videoInfo").classList.toggle("videoActive");
        this.classList.toggle("active");
    });
}

var toggleButton = document.getElementById("toggleTimeSpan");
if (toggleButton) {
    var savedState = localStorage.getItem("activityLog.showTimestamp");
    if (savedState === "true") {
        toggleButton.checked = true;
        document.querySelectorAll(".timestamp").forEach(function(ts) {
            ts.style.display = "inline-block";
        });
    }
    toggleButton.addEventListener("change", function() {
        var show = toggleButton.checked;
        localStorage.setItem("activityLog.showTimestamp", show);
        document.querySelectorAll(".timestamp").forEach(function(timestamp) {
            timestamp.style.display = show ? "inline-block" : "none";
        });
    });
}

// Auto-expand all failed nodes on load (U8)
document.querySelectorAll(".task.failed").forEach(function(failedTask) {
    var nested = failedTask.parentElement.querySelector(".nested");
    if (nested) {
        nested.classList.add("active");
        failedTask.classList.add("task-open");
    }
});

// Expand All / Collapse All (U2)
var expandAllBtn = document.getElementById("expandAll");
var collapseAllBtn = document.getElementById("collapseAll");

if (expandAllBtn) {
    expandAllBtn.addEventListener("click", function() {
        document.querySelectorAll(".nested").forEach(function(el) { el.classList.add("active"); });
        document.querySelectorAll(".task").forEach(function(el) { el.classList.add("task-open"); });
    });
}
if (collapseAllBtn) {
    collapseAllBtn.addEventListener("click", function() {
        document.querySelectorAll(".nested").forEach(function(el) { el.classList.remove("active"); });
        document.querySelectorAll(".task").forEach(function(el) { el.classList.remove("task-open"); });
        document.querySelectorAll(".inInfo").forEach(function(el) { el.classList.remove("inActive"); });
        document.querySelectorAll(".outInfo").forEach(function(el) { el.classList.remove("outActive"); });
        document.querySelectorAll(".attachmentInfo").forEach(function(el) { el.classList.remove("attachmentActive"); });
        document.querySelectorAll(".videoInfo").forEach(function(el) { el.classList.remove("videoActive"); });
        document.querySelectorAll(".longDescription").forEach(function(el) { el.classList.remove("descriptionActive"); });
        document.querySelectorAll(".contentButton").forEach(function(el) { el.classList.remove("active"); });
    });
}

// Expand Failed — collapse all, then open the path to every failed node and scroll to the first
var expandFailedBtn = document.getElementById("expandFailed");
if (expandFailedBtn) {
    expandFailedBtn.addEventListener("click", function() {
        // First collapse everything
        document.querySelectorAll(".nested").forEach(function(el) { el.classList.remove("active"); });
        document.querySelectorAll(".task").forEach(function(el) { el.classList.remove("task-open"); });
        document.querySelectorAll(".inInfo").forEach(function(el) { el.classList.remove("inActive"); });
        document.querySelectorAll(".outInfo").forEach(function(el) { el.classList.remove("outActive"); });
        document.querySelectorAll(".attachmentInfo").forEach(function(el) { el.classList.remove("attachmentActive"); });
        document.querySelectorAll(".longDescription").forEach(function(el) { el.classList.remove("descriptionActive"); });
        document.querySelectorAll(".contentButton").forEach(function(el) { el.classList.remove("active"); });

        // Walk up the DOM from a failed element and open every ancestor .nested
        function expandAncestors(el) {
            var node = el.parentElement;
            while (node) {
                if (node.classList && node.classList.contains("nested")) {
                    node.classList.add("active");
                    // The task span immediately precedes the .nested ul in the DOM
                    var taskSpan = node.previousElementSibling;
                    while (taskSpan) {
                        if (taskSpan.classList && taskSpan.classList.contains("task")) {
                            taskSpan.classList.add("task-open");
                            break;
                        }
                        taskSpan = taskSpan.previousElementSibling;
                    }
                }
                node = node.parentElement;
            }
        }

        // Open description, input, output and attachment panels on the failed <li>
        function expandFailedDetails(failedEl) {
            // .interaction.failed is the <li> itself; .task.failed is a <span> inside <li>
            var li = failedEl.classList.contains("interaction") ? failedEl : failedEl.parentElement;

            var longDesc = li.querySelector(":scope > .longDescription");
            if (longDesc) longDesc.classList.add("descriptionActive");
            var ellipsis = li.querySelector(":scope > .ellipses");
            if (ellipsis) ellipsis.classList.add("active");

            var inInfo = li.querySelector(".inInfo");
            if (inInfo) inInfo.classList.add("inActive");
            var inBtn = li.querySelector(".inContentButton");
            if (inBtn) inBtn.classList.add("active");

            var outInfo = li.querySelector(".outInfo");
            if (outInfo) outInfo.classList.add("outActive");
            var outBtn = li.querySelector(".outContentButton");
            if (outBtn) outBtn.classList.add("active");

            var attInfo = li.querySelector(".attachmentInfo");
            if (attInfo) attInfo.classList.add("attachmentActive");
            var attBtn = li.querySelector(".attachmentContentButton");
            if (attBtn) attBtn.classList.add("active");
        }

        var firstFailed = null;
        document.querySelectorAll(".task.failed, .interaction.failed").forEach(function(failedEl) {
            expandAncestors(failedEl);
            expandFailedDetails(failedEl);
            if (!firstFailed) firstFailed = failedEl;
        });

        if (firstFailed) {
            firstFailed.scrollIntoView({ behavior: "smooth", block: "center" });
        }
    });
}

// Theme toggle — cycles: auto (system) → light → dark → auto
var themeToggleBtn = document.getElementById("themeToggle");
if (themeToggleBtn) {
    var THEME_KEY = "activityLog.theme";

    function updateThemeButton(theme) {
        if (theme === "light") {
            themeToggleBtn.textContent = "\u2600 Light";
            themeToggleBtn.title = "Currently: Light. Click for Dark.";
        } else if (theme === "dark") {
            themeToggleBtn.textContent = "\u263E Dark";
            themeToggleBtn.title = "Currently: Dark. Click for Auto (system).";
        } else {
            var prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
            themeToggleBtn.textContent = "\u25D1 Auto";
            themeToggleBtn.title = "Currently: Auto (" + (prefersDark ? "dark" : "light") + " from system). Click for Light.";
        }
    }

    function applyTheme(theme) {
        if (theme === "light" || theme === "dark") {
            document.documentElement.setAttribute("data-theme", theme);
        } else {
            document.documentElement.removeAttribute("data-theme");
        }
        updateThemeButton(theme);
    }

    // Restore saved preference on load
    var savedTheme = localStorage.getItem(THEME_KEY);
    applyTheme(savedTheme || null);

    themeToggleBtn.addEventListener("click", function() {
        var current = document.documentElement.getAttribute("data-theme");
        var next;
        if (!current) {
            next = "light";
        } else if (current === "light") {
            next = "dark";
        } else {
            next = null; // back to auto
        }
        if (next) {
            localStorage.setItem(THEME_KEY, next);
        } else {
            localStorage.removeItem(THEME_KEY);
        }
        applyTheme(next);
    });
}

// Timestamp emphasis — wrap date/time/microseconds in semantic spans so the
// time part can be styled differently from the date and microsecond portions.
document.querySelectorAll(".timestamp").forEach(function(el) {
    el.innerHTML = el.textContent.replace(
        /(\d{4}-\d{2}-\d{2}) (\d{2}:\d{2}:\d{2})(\.\d+)?/g,
        '<span class="ts-date">$1</span> <span class="ts-time">$2</span><span class="ts-micro">$3</span>'
    );
});
